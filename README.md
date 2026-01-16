<h1 align="center">discord-html-transcript-jda</h1>

<p align="center">
    <strong>A Discord HTML transcript generator that preserves your favorite Discord styles</strong>
    <br>
    <strong><a href="https://github.com/discord-jda/JDA">JDA</a> wrapper for <a href="https://github.com/omardiaadev/discord-html-transcript-api">discord-html-transcript-api</a></strong>
</p>

<p align="center">
    <a href="https://central.sonatype.com/artifact/dev.omardiaa/discord-html-transcript-jda"><img alt="Maven Version" src="https://img.shields.io/maven-central/v/dev.omardiaa/discord-html-transcript-jda?label=Maven&color=0055D2&labelColor=0055D2"/></a>
    <a href="https://github.com/omardiaadev/discord-html-transcript-jda/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/omardiaadev/discord-html-transcript-jda?label=License&color=0055D2&labelColor=0055D2"/></a>
</p>

## About

This is a wrapper for [`discord-html-transcript-api`](https://github.com/omardiaadev/discord-html-transcript-api).

## Installation

##### Requirements

- **Java 21+**

```xml

<dependency>
  <groupId>dev.omardiaa</groupId>
  <artifactId>discord-html-transcript-jda</artifactId>
  <version>1.0.0</version>
</dependency>
```

```kts

implementation("dev.omardiaa:discord-html-transcript-jda:1.0.0")
```

## How To Use

```java
import dev.omardiaa.transcript.api.Transcript;
import dev.omardiaa.transcript.jda.TranscriberClient;

public static void main(String[] args) {
  TranscriberClient client = new TranscriberClient(jda);

  CompletableFuture<Transcript> transcript = client.transcribe(guildId, channelId);
}
```

## Enjoying The Package? Give it A Star!

<a href="https://fiverr.com/skywolfxp"><img alt="Fiverr" src="https://img.shields.io/badge/-1DBF73?style=for-the-badge&logo=fiverr&logoColor=FFF&logoSize=auto"/></a>
<a href="https://discord.gg/fWtQjEJgWX"><img alt="Discord" src="https://img.shields.io/discord/1055244032105787472?style=for-the-badge&logo=discord&logoColor=FFF&logoSize=auto&label=%20&color=5865F2"/></a>
