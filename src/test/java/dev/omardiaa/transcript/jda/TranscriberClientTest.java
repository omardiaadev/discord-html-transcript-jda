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
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * An integration test to verify transcript generation.
 * <br>
 * Additionally, this will generate a {@code transcript.html} of the specified channel under the {@code /target}
 * directory.
 * <p>
 * This test will be skipped if {@link #DISCORD_BOT_TOKEN} or {@link #DISCORD_CHANNEL_ID} environment variables are not
 * specified.
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
  private static TranscriberClient transcriber;

  @BeforeAll
  static void beforeAll() throws InterruptedException {
    jda = JDABuilder.createLight(DISCORD_BOT_TOKEN, GatewayIntent.MESSAGE_CONTENT).build().awaitReady();
    transcriber = new TranscriberClient(jda);
  }

  @AfterAll
  static void afterAll() {
    if (jda != null) {
      jda.shutdownNow();
    }
  }

  @Test
  void shouldTranscribe() throws IOException {
    TextChannel channel = jda.getTextChannelById(DISCORD_CHANNEL_ID);
    assertNotNull(channel, "Channel not found.");

    JDATranscript transcript = assertTimeoutPreemptively(
      Duration.ofSeconds(30),
      () -> transcriber.transcribe(channel).join());

    Path targetDir = Path.of("target");
    Files.createDirectories(targetDir);
    Path filePath = targetDir.resolve("transcript.html");

    transcript.toFile(filePath.toFile());
    LOGGER.info("Saved: file://{}", filePath.toAbsolutePath());
  }
}
