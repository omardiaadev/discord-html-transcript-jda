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

package dev.omardiaa.transcript.jda.util;

import dev.omardiaa.transcript.jda.exception.TranscriberPermissionException;
import dev.omardiaa.transcript.jda.service.TranscriberClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.SelfMember;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jspecify.annotations.NullMarked;

import java.util.EnumSet;
import java.util.Set;

/**
 * A utility class for the {@link TranscriberClient}.
 */
@NullMarked
public final class TranscriberUtil {
  public static final Set<Permission> REQUIRED_PERMISSIONS = Set.of(
    Permission.VIEW_CHANNEL,
    Permission.MESSAGE_HISTORY);

  private TranscriberUtil() {}

  /**
   * Checks the provided {@code jda} instance.
   *
   * @param jda
   *   the JDA instance to check.
   *
   * @throws IllegalStateException
   *   if the provided {@code jda} instance is missing {@link GatewayIntent#MESSAGE_CONTENT}.
   */
  public static void checkJDA(JDA jda) {
    if (!jda.getGatewayIntents().contains(GatewayIntent.MESSAGE_CONTENT)) {
      throw new IllegalStateException("MESSAGE_CONTENT intent must be enabled.");
    }
  }

  /**
   * Checks the provided {@code channel}.
   *
   * @param channel
   *   the channel to check.
   *
   * @throws TranscriberPermissionException
   *   if the {@code jda} instance used is missing any of {@link #REQUIRED_PERMISSIONS} for the provided
   *   {@code channel}.
   */
  public static void checkChannel(GuildMessageChannel channel) {
    SelfMember member = channel.getGuild().getSelfMember();

    EnumSet<Permission> missingPermissions = EnumSet.copyOf(REQUIRED_PERMISSIONS);
    missingPermissions.removeAll(member.getPermissions(channel));

    if (!missingPermissions.isEmpty()) {
      throw new TranscriberPermissionException(channel, missingPermissions);
    }
  }
}
