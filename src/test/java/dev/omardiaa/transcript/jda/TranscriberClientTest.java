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

package dev.omardiaa.transcript.jda;

import dev.omardiaa.transcript.jda.model.JDATranscript;
import dev.omardiaa.transcript.jda.service.TranscriberClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;

/**
 * Integration test to verify transcript generation.
 * <br>
 * Additionally, this will generate a {@code transcript.html}
 * of the generated transcript under {@code /target} directory.
 *
 * @apiNote This test will only run when both {@link #DISCORD_BOT_TOKEN} and {@link #DISCORD_CHANNEL_ID}
 * environment variables are specified.
 */
@EnabledIfEnvironmentVariables({
  @EnabledIfEnvironmentVariable(named = "DISCORD_BOT_TOKEN", matches = ".+"),
  @EnabledIfEnvironmentVariable(named = "DISCORD_CHANNEL_ID", matches = ".+")
})
class TranscriberClientTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(TranscriberClientTest.class);

  private static final String DISCORD_BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");
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

    TextChannel channel = jda.getTextChannelById(DISCORD_CHANNEL_ID);

    assertNotNull(channel);

    JDATranscript transcript = assertTimeout(
      Duration.ofMinutes(3),
      () -> new TranscriberClient(jda)
        .transcribe(channel)
        .thenApply(t -> {
          LOGGER.info("Finished");
          return t;
        })
        .join());

    Path dir = Paths.get("target");
    Files.createDirectories(dir);
    Path filePath = dir.resolve("transcript.html");

    transcript.toFile(filePath.toFile());
    LOGGER.info("Transcript: file://{}", filePath.toAbsolutePath());

    assertNotNull(transcript);
  }
}
