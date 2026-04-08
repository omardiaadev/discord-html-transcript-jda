<h1 align="center">discord-html-transcript-jda</h1>

<p align="center">
    <strong>Generate natively styled Discord chat logs with JDA</strong>
    <br>
    <a href="https://github.com/omardiaadev/discord-html-transcript">discord-html-transcript</a> wrapper for Java Discord API
</p>

<p align="center">
    <a href="https://central.sonatype.com/artifact/dev.omardiaa/discord-html-transcript-jda"><img alt="Maven Version" src="https://img.shields.io/maven-central/v/dev.omardiaa/discord-html-transcript-jda?label=Maven&color=0559D2"></a>
    <a href="https://github.com/omardiaadev/discord-html-transcript"><img alt="discord-html-transcript" src="https://img.shields.io/github/v/tag/omardiaadev/discord-html-transcript?filter=0.1.0-beta.1&label=discord-html-transcript&color=0559D2"></a>
    <a href="https://github.com/omardiaadev/discord-html-transcript-jda/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/omardiaadev/discord-html-transcript-jda?label=License&color=0559D2"></a>
    <br>
    <a href="https://discord.gg/fWtQjEJgWX"><img alt="Discord" src="https://img.shields.io/badge/Discord-5865F2?logo=discord&logoColor=FFF&color=5865F2"></a>
</p>

<details>
    <summary>Table of Contents</summary>
    <ul>
        <li><a href="#features">Features</a></li>
        <li><a href="#preview">Preview</a></li>
        <li><a href="#getting-started">Getting Started</a></li>
        <li><a href="#usage">Usage</a></li>
        <li><a href="#contributing">Contributing</a></li>
    </ul>
</details>

## Features

- **JDA Integration:** Retrieve messages easily with your JDA client instance.
- **Beautiful UI:** Modern HTML/CSS that has the look and feel of the Discord desktop client.
- **Asynchronous:** Built with `CompletableFuture` for non-blocking performance.

## Preview

<a title="Click To View Full Preview" href="https://htmlpreview.github.io/?https://github.com/omardiaadev/discord-html-transcript/blob/main/examples/transcript.html">
    <img alt="Preview" src="https://res.cloudinary.com/omardiaadev/image/upload/discord-html-transcript_ocjq03.png">
</a>

## Getting Started

### Prerequisites

- **Java 17+**
- Your bot must enable the following [intents](https://docs.discord.com/developers/quick-start/getting-started#what-are-intents):
    - Message Content

### Installation

<img alt="Maven" src="https://img.shields.io/badge/Maven-C71A36?logo=apachemaven">

```xml

<dependency>
  <groupId>dev.omardiaa</groupId>
  <artifactId>discord-html-transcript-jda</artifactId>
  <version>0.1.0-beta.1</version>
</dependency>
```

<img alt="Gradle" src="https://img.shields.io/badge/Gradle-02303A?logo=gradle">

```kotlin

implementation("dev.omardiaa:discord-html-transcript-jda:0.1.0-beta.1")
```

## Usage

### Example: Slash Command

```java
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import dev.omardiaa.transcript.jda.service.TranscriberClient;
import dev.omardiaa.transcript.jda.exception.TranscriberPermissionException;

public class SlashCommandListener extends ListenerAdapter {
  private final TranscriberClient client;

  public SlashCommandListener(JDA jda) {
    this.client = new TranscriberClient(jda);
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    if (event.getName().equals("transcript")) {
      // acknowledge the interaction before Discord expires it
      event.deferReply().queue();

      client.transcribe(event.getChannel())
            .thenAccept(transcript -> {
              // upload and send the transcript
              event.getHook().sendFiles(transcript.toFileUpload()).queue();
            })
            .exceptionally(throwable -> {
              if (throwable.getCause() instanceof TranscriberPermissionException ex) {
                // handle permission exception
                event.getHook()
                     .sendMessageFormat(
                       "Failed to generate transcript due to missing '%s' permission.",
                       ex.getPermission().getName())
                     .queue();
              } else {
                // handle other exceptions
                event.getHook().sendMessage("Failed to generate transcript due to unknown exception.").queue();
              }

              return null;
            });
    }
  }
}
```

## Contributing

**If you found `discord-html-transcript-jda` useful, please consider giving it a 🌟!**

Need help? [Ask the Community](https://discord.omardiaa.dev)!

<div align="center">
    <p>Made With ❤️ By <a href="https://github.com/omardiaadev"><b>Omar Diaa</b></a></p>
    <a href="https://fiverr.com/skywolfxp"><img alt="Fiverr" src="https://img.shields.io/badge/-1DBF73?style=for-the-badge&logo=fiverr&logoColor=FFF&logoSize=auto"></a>
    <a href="https://ko-fi.com/omardiaadev"><img alt="Ko-fi" src="https://img.shields.io/badge/ko--fi-FF6433?style=for-the-badge&logo=kofi&logoColor=FFF"></a>
</div>
