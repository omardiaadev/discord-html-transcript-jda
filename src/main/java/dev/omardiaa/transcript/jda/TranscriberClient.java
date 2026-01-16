package dev.omardiaa.transcript.jda;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.omardiaa.transcript.api.Transcriber;
import dev.omardiaa.transcript.api.Transcript;
import dev.omardiaa.transcript.api.schema.Payload;
import dev.omardiaa.transcript.api.schema.payload.Channel;
import dev.omardiaa.transcript.api.schema.payload.Guild;
import dev.omardiaa.transcript.api.schema.payload.Message;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.Route;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class TranscriberClient {
  private final JDA jda;
  private final Transcriber transcriber;

  public TranscriberClient(JDA jda) {
    this.jda = jda;
    this.transcriber = new Transcriber();
  }

  public CompletableFuture<Transcript> transcribe(String guildId, String channelId) {
    if (!jda.getGatewayIntents().contains(GatewayIntent.MESSAGE_CONTENT)) {
      return CompletableFuture.failedFuture(new IllegalStateException("MESSAGE_CONTENT is disabled."));
    }

    CompletableFuture<Channel> channelFuture = new JacksonRestAction<>(
      jda
      , Route.Channels.GET_CHANNEL.compile(channelId),
      new TypeReference<Channel>() {})
      .submit();

    CompletableFuture<Guild> guildFuture = new JacksonRestAction<>(
      jda,
      Route.Guilds.GET_GUILD.compile(guildId),
      new TypeReference<Guild>() {})
      .submit();

    CompletableFuture<List<Message>> messagesFuture = fetchMessagePage(channelId, null, new ArrayList<>());

    return CompletableFuture
      .allOf(channelFuture, guildFuture, messagesFuture)
      .thenComposeAsync(v -> transcriber.transcribe(
        new Payload(guildFuture.join(), channelFuture.join(), messagesFuture.join().reversed(), null)));
  }

  private CompletableFuture<List<Message>> fetchMessagePage(
    String channelId, @Nullable String lastMessageId, List<Message> accumulator) {
    Route.CompiledRoute route = Route.Messages.GET_MESSAGE_HISTORY.compile(channelId).withQueryParams("limit", "100");

    if (lastMessageId != null) {
      route = route.withQueryParams("before", lastMessageId);
    }

    JacksonRestAction<List<Message>> action = new JacksonRestAction<>(jda, route, new TypeReference<>() {});

    return action.submit().thenComposeAsync(batch -> {
      if (batch.isEmpty()) {
        return CompletableFuture.completedStage(accumulator);
      }

      accumulator.addAll(batch);

      if (batch.size() < 100) {
        return CompletableFuture.completedStage(accumulator);
      }

      return fetchMessagePage(channelId, batch.getLast().getId(), accumulator);
    });
  }
}
