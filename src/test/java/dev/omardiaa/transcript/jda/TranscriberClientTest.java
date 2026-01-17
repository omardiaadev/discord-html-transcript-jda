package dev.omardiaa.transcript.jda;

import dev.omardiaa.transcript.api.Transcript;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;

/**
 * Integration test to verify transcript generation.
 * <p> Additionally, this will generate a {@code transcript.html}
 * of the generated transcript under {@code /target} directory.
 *
 * @apiNote This test will only run when all {@link #DISCORD_BOT_TOKEN}, {@link #DISCORD_GUILD_ID}, and
 * {@link #DISCORD_CHANNEL_ID}
 * environment variables are specified.
 */
@EnabledIfEnvironmentVariables({
  @EnabledIfEnvironmentVariable(named = "DISCORD_BOT_TOKEN", matches = ".+"),
  @EnabledIfEnvironmentVariable(named = "DISCORD_GUILD_ID", matches = ".+"),
  @EnabledIfEnvironmentVariable(named = "DISCORD_CHANNEL_ID", matches = ".+")})
class TranscriberClientTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(TranscriberClientTest.class);

  private static final String DISCORD_BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
  private static final String DISCORD_GUILD_ID = System.getenv("DISCORD_GUILD_ID");
  private static final String DISCORD_CHANNEL_ID = System.getenv("DISCORD_CHANNEL_ID");

  private static JDA jda;

  @BeforeAll
  static void beforeAll() throws InterruptedException {
    jda = JDABuilder.createLight(DISCORD_BOT_TOKEN, GatewayIntent.MESSAGE_CONTENT).build().awaitReady();
  }

  @AfterAll
  static void afterAll() {
    if (jda != null) {
      jda.shutdownNow();
    }
  }

  @Test
  void transcribe() throws IOException {
    LOGGER.info("Started");

    Transcript transcript = assertTimeout(
      Duration.ofMinutes(3),
      () -> new TranscriberClient(jda)
        .transcribe(DISCORD_GUILD_ID, DISCORD_CHANNEL_ID)
        .thenApply(t -> {
          LOGGER.info("Finished");
          return t;
        })
        .join());

    Path dir = Paths.get("target");
    Files.createDirectories(dir);
    Path filePath = dir.resolve("transcript.html");

    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
      fos.write(transcript.getByteArray());
      LOGGER.info("file://{}", filePath.toAbsolutePath());
    }

    assertNotNull(transcript);
  }
}
