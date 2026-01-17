package dev.omardiaa.transcript.jda;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.omardiaa.transcript.api.config.TranscriberConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.exceptions.ParsingException;
import net.dv8tion.jda.api.requests.Route;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
class JacksonRestAction<T> extends RestActionImpl<T> {
  JacksonRestAction(JDA jda, Route.CompiledRoute route, TypeReference<T> responseType) {
    super(
      jda, route, (response, request) -> {
        try {
          return TranscriberConfig.getObjectMapper().readValue(response.getString(), responseType);
        } catch (IOException e) {
          throw new ParsingException("Failed to parse JSON response using Jackson", e);
        }
      });
  }
}
