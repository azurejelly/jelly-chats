# jelly-chats

JellyChats is a highly customizable Redis-based private chats plugin for [Velocity](https://github.com/PaperMC/Velocity) proxies.

## Pre-requisites

For building from source:
- JDK 17 or higher
- An Internet connection

For running the plugin:
- A Redis instance
- JRE 17 or higher
- A [Velocity](https://github.com/PaperMC/Velocity) proxy

## Installation

Grab the latest release from [here](https://github.com/azurejelly/jelly-chats), then drop it into your `plugins/` folder.

### Creating new chats

Go to the `plugins/jelly-chats/config.yml` file in your server, then add a new entry inside the `chats` section:
```yaml
  moderator:
    enabled: true
    name: "Moderators"
    channel: "jelly-chats/mods"
    permission: "jelly-chats.chat.moderator"
    command:
      main: "mod-chat"
      aliases:
        - "m"
        - "mc"
        - "modchat"
        - "mods"
        - "mod"
```

You can also simplify it to:
```yaml
  moderator:
    enabled: true
```

In these cases:
- The name will be set to the chat ID
  - `moderator` in this example
- The Redis channel will be set to the value of `default-channel-prefix` + the chat ID
  - `jelly-chats/moderator` in this example
- The permission will be set to `jelly-chats.chat.` + the chat ID
  - `jelly-chats.chat.moderator` in this example
- The command will be the chat ID + `-chat`
  - `/moderator-chat` in this example
- The command will have no aliases

### Formatting new chats

Simply add a new entry under `messages → formatting` with the same chat ID:

```yaml
messages:
  # ...
  formatting:
    moderator: "<gold>[MOD]</gold> <yellow><author>:</yellow> <content>"
```

This example assumes you're using the same chat created earlier.

## Building from source

Clone the repository:
```shell
$ git clone https://github.com/azurejelly/jelly-chats
$ cd jelly-chats/
```

Build the project using Gradle:
```shell
$ ./gradlew build
```

You should be able to find a distributable JAR under `./build/libs/`