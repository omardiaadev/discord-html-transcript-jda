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

package dev.omardiaa.transcript.jda.model;

import dev.omardiaa.transcript.core.model.AbstractTranscript;
import gg.jte.output.Utf8ByteOutput;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jspecify.annotations.NullMarked;

/**
 * A {@link JDA} implementation of {@link AbstractTranscript} that provides utilities for JDA's {@link FileUpload}.
 */
@NullMarked
public final class JDATranscript extends AbstractTranscript {
  /**
   * Constructs a new {@link JDATranscript} instance with the provided byte output.
   *
   * @param output
   *   the {@link Utf8ByteOutput} of the transcribed channel.
   */
  public JDATranscript(Utf8ByteOutput output) {
    super(output);
  }

  /**
   * Writes {@link Utf8ByteOutput#toByteArray()} into JDA's {@link FileUpload}.
   * <p>
   * The filename of this {@link FileUpload} is always {@code transcript.html},
   * if you want to specify a custom filename, use {@link #toFileUpload(String)}.
   *
   * @return {@link FileUpload} to send directly through JDA events.
   */
  public FileUpload toFileUpload() {
    return toFileUpload("transcript.html");
  }

  /**
   * Writes {@link Utf8ByteOutput#toByteArray()} into JDA's {@link FileUpload}.
   *
   * @param filename
   *   Name to use for the {@link FileUpload}.
   *   If {@code filename} does not end with {@code .html}, it will be appended automatically.
   *
   * @return {@link FileUpload} to send directly through JDA events.
   */
  public FileUpload toFileUpload(String filename) {
    return FileUpload.fromData(
      getOutput().toByteArray(),
      filename.endsWith(".html") ? filename : filename + ".html");
  }
}
