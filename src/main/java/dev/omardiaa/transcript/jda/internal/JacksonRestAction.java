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

package dev.omardiaa.transcript.jda.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.omardiaa.transcript.core.config.TranscriberConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.exceptions.ParsingException;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.Route;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

/**
 * {@link RestAction} implementation to fetch raw response objects from the Discord API.
 *
 * @param <T>
 *   The generic to use for deserialization of the raw response object.
 */
@NullMarked
public class JacksonRestAction<T> extends RestActionImpl<T> {
  /**
   * @param jda
   *   The JDA instance to use for fetching.
   * @param route
   *   The route to fetch.
   * @param responseType
   *   The type to use for deserialization.
   */
  public JacksonRestAction(JDA jda, Route.CompiledRoute route, TypeReference<T> responseType) {
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
