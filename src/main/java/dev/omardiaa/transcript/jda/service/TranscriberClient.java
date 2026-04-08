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
import dev.omardiaa.transcript.core.model.payload.PayloadOptions;
import dev.omardiaa.transcript.core.service.Transcriber;
import dev.omardiaa.transcript.jda.exception.TranscriberPermissionException;
import dev.omardiaa.transcript.jda.model.JDATranscript;
import dev.omardiaa.transcript.jda.util.TranscriberUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A class for generating the transcripts.
 */
@NullMarked
public class TranscriberClient {
  private final Transcriber transcriber;
  private final TranscriberFetcher fetcher;

  /**
   * Constructs a new {@link TranscriberClient} instance.
   *
   * @param jda
   *   the {@link JDA} instance used to fetch the {@link Payload}.
   *
   * @throws IllegalStateException
   *   if the provided {@code jda} instance is missing {@link GatewayIntent#MESSAGE_CONTENT}.
   */
  public TranscriberClient(JDA jda) {
    TranscriberUtil.checkJDA(jda);
    this.transcriber = new Transcriber();
    this.fetcher = new TranscriberFetcher(jda);
  }

  /**
   * Provides a {@link CompletableFuture} of the transcript generation task for the provided {@code channel}.
   * <p>
   * This {@link CompletableFuture} completes exceptionally with {@link TranscriberPermissionException}
   * if the {@code jda} instance used is missing any of {@link TranscriberUtil#REQUIRED_PERMISSIONS} for the provided
   * {@code channel}.
   *
   * @param channel
   *   the {@link GuildMessageChannel} to transcribe.
   *
   * @return {@link CompletableFuture} of a {@link JDATranscript}.
   */
  public CompletableFuture<JDATranscript> transcribe(GuildMessageChannel channel) {
    return transcribe(channel, new PayloadOptions());
  }

  /**
   * Provides a {@link CompletableFuture} of the transcript generation task for the provided {@code channel}.
   * <p>
   * This {@link CompletableFuture} completes exceptionally with {@link TranscriberPermissionException}
   * if the {@code jda} instance used is missing any of {@link TranscriberUtil#REQUIRED_PERMISSIONS} for the provided
   * {@code channel}.
   *
   * @param channel
   *   the {@link GuildMessageChannel} to transcribe.
   * @param options
   *   the {@link PayloadOptions} to use.
   *
   * @return {@link CompletableFuture} of a {@link JDATranscript}.
   */
  public CompletableFuture<JDATranscript> transcribe(GuildMessageChannel channel, PayloadOptions options) {
    try {
      TranscriberUtil.checkChannel(channel);
    } catch (TranscriberPermissionException e) {
      return CompletableFuture.failedFuture(e);
    }

    CompletableFuture<Guild> guildFuture = fetcher.getGuild(channel);
    CompletableFuture<Channel> channelFuture = fetcher.getChannel(channel);
    CompletableFuture<List<Message>> messagesFuture = fetcher.getMessages(channel);

    return CompletableFuture
      .allOf(
        guildFuture,
        channelFuture,
        messagesFuture)
      .thenApply(v -> new Payload(
        guildFuture.join(),
        channelFuture.join(),
        messagesFuture.join(),
        options))
      .thenCompose(transcriber::transcribe)
      .thenApply(JDATranscript::new);
  }
}
