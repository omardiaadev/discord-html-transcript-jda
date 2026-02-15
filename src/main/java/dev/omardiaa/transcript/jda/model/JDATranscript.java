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
   *   Raw byte output of the transcribed channel.
   */
  public JDATranscript(Utf8ByteOutput output) {
    super(output);
  }

  /**
   * Writes {@link Utf8ByteOutput#toByteArray()} into JDA's {@link FileUpload}.
   *
   * @return {@link FileUpload} to send directly in JDA interactions.
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
   * @return {@link FileUpload} to send directly in JDA interactions.
   */
  public FileUpload toFileUpload(String filename) {
    return FileUpload.fromData(
      this.getOutput().toByteArray(),
      filename.endsWith(".html") ? filename : filename + ".html");
  }
}
