package dev.omardiaa.transcript.jda;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.omardiaa.transcript.api.config.TranscriberConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.exceptions.ParsingException;
import net.dv8tion.jda.api.requests.Request;
import net.dv8tion.jda.api.requests.Response;
import net.dv8tion.jda.api.requests.Route;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public class JacksonRestAction<T> extends RestActionImpl<T> {
  private final TypeReference<T> responseType;

  public JacksonRestAction(JDA jda, Route.CompiledRoute route, TypeReference<T> responseType) {
    super(jda, route);
    this.responseType = responseType;
  }

  @Override
  public void handleResponse(Response response, Request<T> request) {
    if (response.isOk()) {
      try {
        request.onSuccess(TranscriberConfig.getObjectMapper().readValue(response.getString(), responseType));
      } catch (IOException e) {
        request.onFailure(new ParsingException("Failed to parse JSON response using Jackson", e));
      } catch (Exception e) {
        request.onFailure(e);
      }
    } else {
      request.onFailure(response);
    }
  }
}
