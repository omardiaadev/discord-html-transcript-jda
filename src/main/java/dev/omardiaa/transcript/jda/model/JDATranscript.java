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
import net.dv8tion.jda.api.utils.FileUpload;
import org.jspecify.annotations.NullMarked;

/**
 * {@link AbstractTranscript} implementation for JDA.
 */
@NullMarked
public final class JDATranscript extends AbstractTranscript {
  /**
   * @param output
   *   The {@link Utf8ByteOutput} of the transcribed channel.
   */
  public JDATranscript(Utf8ByteOutput output) {
    super(output);
  }

  /**
   * Writes {@link Utf8ByteOutput#toByteArray()} into JDA's {@link FileUpload}.
   * <p>
   * The filename of this {@link FileUpload} is always {@code transcript.html},
   * if you want to specify your own filename, see {@link #toFileUpload(String)}.
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
   *   If {@code filename} does not end with {@code .html}, it will be automatically appended.
   *
   * @return {@link FileUpload} to send directly through JDA events.
   */
  public FileUpload toFileUpload(String filename) {
    return FileUpload.fromData(
      this.getOutput().toByteArray(),
      filename.endsWith(".html") ? filename : filename + ".html");
  }
}
