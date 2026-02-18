<h1 align="center">discord-html-transcript-jda</h1>

<p align="center">
    <strong>Generate styled archives for your tickets and chats with ease</strong>
    <br>
    <a href="https://github.com/discord-jda/JDA">Java Discord API</a> wrapper for <a href="https://github.com/omardiaadev/discord-html-transcript">discord-html-transcript</a>
</p>

<p align="center">
    <a href="https://central.sonatype.com/artifact/dev.omardiaa/discord-html-transcript-jda"><img alt="Maven Version" src="https://img.shields.io/maven-central/v/dev.omardiaa/discord-html-transcript-jda?label=Maven&color=0055D2"/></a>
    <a href="https://github.com/omardiaadev/discord-html-transcript-jda/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/omardiaadev/discord-html-transcript-jda?label=License&color=0055D2"/></a>
    <a href="https://discord.gg/fWtQjEJgWX"><img alt="Discord" src="https://img.shields.io/badge/Discord-5865F2?logo=discord&logoColor=FFF&color=5865F2"/></a>
</p>

<details>
    <summary>Contents</summary>
    <ul>
        <li><a href="#features">Features</a></li>
        <li><a href="#preview">Preview</a></li>
        <li><a href="#getting-started">Getting Started</a></li>
        <li><a href="#usage">Usage</a></li>
    </ul>
</details>

## Features

- **Beautiful UI:** Modern HTML/CSS that has the look and feel of the Discord desktop client.
- **Asynchronous:** Built with `CompletableFuture` for non-blocking performance.
- **JDA Integration:** Support for `FileUpload` making it easy to send transcripts with JDA.

## Preview

[Full Preview](https://htmlpreview.github.io/?https://github.com/omardiaadev/discord-html-transcript/blob/main/examples/example-transcript.html)

<a href="https://htmlpreview.github.io/?https://github.com/omardiaadev/discord-html-transcript/blob/main/examples/example-transcript.html">
    <img alt="discord-html-transcript" src="https://res.cloudinary.com/omardiaadev/image/upload/v1771423142/discord-html-transcript_ocjq03.png"/>
</a>

## Getting Started

### Requirements

- **Java 17+**

### Installation

#### Maven

```xml

<dependency>
  <groupId>dev.omardiaa</groupId>
  <artifactId>discord-html-transcript-jda</artifactId>
  <version>0.1.0-beta.1</version>
</dependency>
```

#### Gradle

```kotlin

implementation("dev.omardiaa:discord-html-transcript-jda:0.1.0-beta.1")
```

## Usage

### Example: Slash Command

```java
public class SlashCommandListener extends ListenerAdapter {
  private final TranscriberClient client;

  public SlashCommandListener(JDA jda) {
    this.client = new TranscriberClient(jda);
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    if (event.getName().equals("transcript")) {
      client.transcribe(event.getChannel())
            .thenAccept(transcript -> {
              event.getHook().sendFiles(transcript.toFileUpload()).queue();
            })
            .exceptionally(throwable -> {
              event.getHook().sendMessage("Failed to generate transcript!").queue();
              return null;
            });
    }
  }
}
```

## 💖 Support The Project

If you found this useful, please consider giving it a 🌟!

<a href="https://fiverr.com/skywolfxp"><img alt="Fiverr" src="https://img.shields.io/badge/-1DBF73?style=for-the-badge&logo=fiverr&logoColor=FFF&logoSize=auto"/></a>
<a href="https://ko-fi.com/omardiaadev"><img alt="Ko-fi" src="https://img.shields.io/badge/ko--fi-FF6433?style=for-the-badge&logo=kofi&logoColor=FFF"/></a>
