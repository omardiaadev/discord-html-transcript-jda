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
    CompletableFuture<Guild> guildFuture = transcriberFetcher.getGuild(channel.getGuild().getId());
    CompletableFuture<Channel> channelFuture = transcriberFetcher.getChannel(channel.getId());
    CompletableFuture<List<Message>> messagesFuture = transcriberFetcher.getMessages(channel.getId());

    return CompletableFuture
      .allOf(channelFuture, guildFuture, messagesFuture)
      .thenApply(v -> new Payload(guildFuture.join(), channelFuture.join(), messagesFuture.join(), null))
      .thenComposeAsync(transcriber::transcribe)
      .thenApply(JDATranscript::new);
  }
}
