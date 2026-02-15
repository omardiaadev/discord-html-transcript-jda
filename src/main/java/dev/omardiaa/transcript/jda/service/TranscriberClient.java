/*
 * Copyright 2026 Omar Diaa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.omardiaa.transcript.jda.service;

import dev.omardiaa.transcript.core.model.Payload;
import dev.omardiaa.transcript.core.model.payload.Channel;
import dev.omardiaa.transcript.core.model.payload.Guild;
import dev.omardiaa.transcript.core.model.payload.Message;
import dev.omardiaa.transcript.core.service.Transcriber;
import dev.omardiaa.transcript.jda.model.JDATranscript;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The Transcriber Client used to generate the transcripts of a {@link GuildMessageChannel}.
 */
@NullMarked
public class TranscriberClient {
  private final Transcriber transcriber;
  private final TranscriberFetcher transcriberFetcher;

  /**
   * @param jda
   *   The {@link JDA} instance used to fetch the {@link Payload}.
   *
   * @throws IllegalStateException
   *   If the specified {@code jda} instance disables {@link GatewayIntent#MESSAGE_CONTENT}.
   */
  public TranscriberClient(JDA jda) {
    if (!jda.getGatewayIntents().contains(GatewayIntent.MESSAGE_CONTENT)) {
      throw new IllegalStateException("MESSAGE_CONTENT intent must be enabled.");
    }

    this.transcriber = new Transcriber();
    this.transcriberFetcher = new TranscriberFetcher(jda);
  }

  /**
   * @param channel
   *   The {@link GuildMessageChannel} to transcribe.
   *
   * @return {@link CompletableFuture} of {@link JDATranscript}.
   */
  public CompletableFuture<JDATranscript> transcribe(GuildMessageChannel channel) {
    String guildId = channel.getGuild().getId();
    String channelId = channel.getId();

    CompletableFuture<Guild> guildFuture = transcriberFetcher.getGuild(guildId);
    CompletableFuture<Channel> channelFuture = transcriberFetcher.getChannel(channelId);
    CompletableFuture<List<Message>> messagesFuture = transcriberFetcher
      .getMessages(channelId)
      .thenApply(messages -> {
        Collections.reverse(messages);
        return messages;
      });

    return CompletableFuture
      .allOf(channelFuture, guildFuture, messagesFuture)
      .thenApply(v -> new Payload(guildFuture.join(), channelFuture.join(), messagesFuture.join(), null))
      .thenComposeAsync(transcriber::transcribe)
      .thenApply(JDATranscript::new);
  }
}
