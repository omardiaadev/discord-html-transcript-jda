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

import com.fasterxml.jackson.core.type.TypeReference;
import dev.omardiaa.transcript.core.model.payload.Channel;
import dev.omardiaa.transcript.core.model.payload.Guild;
import dev.omardiaa.transcript.core.model.payload.Message;
import dev.omardiaa.transcript.jda.internal.JacksonRestAction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.Route;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@NullMarked
class TranscriberFetcher {
  private final JDA jda;

  TranscriberFetcher(JDA jda) {
    this.jda = jda;
  }

  CompletableFuture<Channel> getChannel(String channelId) {
    return new JacksonRestAction<>(
      jda,
      Route.Channels.GET_CHANNEL.compile(channelId),
      new TypeReference<Channel>() {})
      .submit();
  }

  CompletableFuture<Guild> getGuild(String guildId) {
    return new JacksonRestAction<>(
      jda,
      Route.Guilds.GET_GUILD.compile(guildId),
      new TypeReference<Guild>() {})
      .submit();
  }

  CompletableFuture<List<Message>> getMessages(String channelId) {
    return getMessagePage(channelId, null, new ArrayList<>());
  }

  private CompletableFuture<List<Message>> getMessagePage(
    String channelId, @Nullable String lastMessageId, List<Message> accumulator) {
    Route.CompiledRoute route = Route.Messages.GET_MESSAGE_HISTORY.compile(channelId).withQueryParams("limit", "100");

    if (lastMessageId != null) {
      route = route.withQueryParams("before", lastMessageId);
    }

    return new JacksonRestAction<>(jda, route, new TypeReference<List<Message>>() {})
      .submit()
      .thenCompose(batch -> {
        accumulator.addAll(batch);

        if (batch.size() < 100) {
          return CompletableFuture.completedStage(accumulator);
        }

        return getMessagePage(channelId, batch.get(batch.size() - 1).getId(), accumulator);
      });
  }
}
