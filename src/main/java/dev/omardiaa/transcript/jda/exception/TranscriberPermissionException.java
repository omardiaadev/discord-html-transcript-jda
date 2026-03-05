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

package dev.omardiaa.transcript.jda.exception;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

/**
 * Indicates that the currently logged in account does not meet the specified {@link Permission}.
 */
public class TranscriberPermissionException extends InsufficientPermissionException {
  public TranscriberPermissionException(GuildChannel channel, Permission permission) {
    super(
      channel,
      permission,
      "JDA instance must have '"
      + permission.getName()
      + "' permission to transcribe #"
      + channel.getName()
      + " ("
      + channel.getId()
      + ")");
  }
}
