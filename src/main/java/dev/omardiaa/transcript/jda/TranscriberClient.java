package dev.omardiaa.transcript.jda;

import dev.omardiaa.transcript.api.Transcriber;
import dev.omardiaa.transcript.api.Transcript;
import dev.omardiaa.transcript.api.schema.Payload;
import dev.omardiaa.transcript.api.schema.payload.Channel;
import dev.omardiaa.transcript.api.schema.payload.Guild;
import dev.omardiaa.transcript.api.schema.payload.Message;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class TranscriberClient {
  private final Transcriber transcriber;
  private final TranscriberClientFetcher fetcher;

  public TranscriberClient(JDA jda) {
    if (!jda.getGatewayIntents().contains(GatewayIntent.MESSAGE_CONTENT)) {
      throw new IllegalStateException("MESSAGE_CONTENT intent must be enabled.");
    }

    this.transcriber = new Transcriber();
    this.fetcher = new TranscriberClientFetcher(jda);
  }

  public CompletableFuture<Transcript> transcribe(String guildId, String channelId) {
    CompletableFuture<Channel> channelFuture = fetcher.getChannel(channelId);
    CompletableFuture<Guild> guildFuture = fetcher.getGuild(guildId);
    CompletableFuture<List<Message>> messagesFuture = fetcher.getMessages(channelId);

    return CompletableFuture
      .allOf(channelFuture, guildFuture, messagesFuture)
      .thenApply(v -> new Payload(guildFuture.join(), channelFuture.join(), messagesFuture.join().reversed(), null))
      .thenComposeAsync(transcriber::transcribe);
  }
}
