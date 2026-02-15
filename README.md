<h1 align="center">discord-html-transcript-jda</h1>

<p align="center">
    <strong>A Discord HTML transcript generator that preserves your favorite Discord styles</strong>
    <br>
    <strong>For <a href="https://github.com/discord-jda/JDA">JDA</a> Users</strong>
</p>

<p align="center">
    <a href="https://central.sonatype.com/artifact/dev.omardiaa/discord-html-transcript-jda"><img alt="Maven Version" src="https://img.shields.io/maven-central/v/dev.omardiaa/discord-html-transcript-jda?label=Maven&labelColor=0055D2&color=FFFFFF"/></a>
    <a href="https://github.com/omardiaadev/discord-html-transcript-jda/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/omardiaadev/discord-html-transcript-jda?label=License&labelColor=0055D2&color=FFFFFF"/></a>
</p>

## About

`discord-html-transcript-jda` is a JDA (Java Discord API) wrapper for [discord-html-transcript](https://github.com/omardiaadev/discord-html-transcript).

<details>
    <summary><strong>Contents</strong></summary>
    <ul>
        <li><a href="#getting-started">Getting Started</a></li>
        <li><a href="#usage">Usage</a></li>
    </ul>
</details>

## Getting Started

### Requirements

- **Java 17+**

### Installation

```xml

<dependency>
  <groupId>dev.omardiaa</groupId>
  <artifactId>discord-html-transcript-jda</artifactId>
  <version>0.1.0-beta.1</version>
</dependency>
```

```kts

implementation("dev.omardiaa:discord-html-transcript-jda:0.1.0-beta.1")
```

## Usage

```java
import dev.omardiaa.transcript.jda.model.JDATranscript;
import dev.omardiaa.transcript.jda.service.TranscriberClient;

public static void main(String[] args) {
  TranscriberClient client = new TranscriberClient(jda);

  CompletableFuture<JDATranscript> transcript = client.transcribe(channel);
}
```

## Enjoying The Package? Give it A Star!

<a href="https://fiverr.com/skywolfxp"><img alt="Fiverr" src="https://img.shields.io/badge/-1DBF73?style=for-the-badge&logo=fiverr&logoColor=FFF&logoSize=auto"/></a>
<a href="https://discord.gg/fWtQjEJgWX"><img alt="Discord" src="https://img.shields.io/discord/1055244032105787472?style=for-the-badge&logo=discord&logoColor=FFF&logoSize=auto&label=%20&color=5865F2"/></a>
