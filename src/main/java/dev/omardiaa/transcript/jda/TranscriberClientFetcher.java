package dev.omardiaa.transcript.jda;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.omardiaa.transcript.api.schema.payload.Channel;
import dev.omardiaa.transcript.api.schema.payload.Guild;
import dev.omardiaa.transcript.api.schema.payload.Message;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.Route;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@NullMarked
class TranscriberClientFetcher {
  private final JDA jda;

  TranscriberClientFetcher(JDA jda) {
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

        return getMessagePage(channelId, batch.getLast().getId(), accumulator);
      });
  }
}
