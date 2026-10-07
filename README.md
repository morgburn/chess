# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[Sequence Diagrams Link]([https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmyyKp8izfL8-yAmMSxXKBgpAf6IzKUR8xqSCGk7HsOmYAZ8K+s6XYwAgQnihignCQSRJgKSb6GLuNL7gyTJTipXI3gFd5LsKYoSm6MpymW7xKpgKrBhqbrarq+qhUYEBqGgADkzBWmi4W8pF4lUEiMA9n225+ZV-rMtMl7QEgABeKAcFGMZxoUelJpUolpk4ACMBE5qoeamYWxbQPUPgtXqbWdbsdFNsODqDR2VUuhu7pbr5gq+fUNRIAAZpYTT6H8OwYhZAJrHF2ikql6owAAkmgIDQCi4AwA9DFHdtoGukt8QrV1PUoLGCnofCybIKmMDpuNoxjJN00FmMRYlgt4OQ2tjZAw5gqbfSMCHnIKDPvE56Xte5MClFK5rgGDOHaT222fU7nihkqgATZjzASD1SGYR5bEaR3wUVR9YkahDYDYjJRgDheEEaFNFkWMcuIQrMvrQxnjeH4-heCg6AxHEiSW9b7m+FgonHeLpYNNIEb8RG7QRt0PRyaoCnDBRK15AU9QADz60h5Tw-pIv+qHUAdeHaBRzH6Bx7ZrudvUzn2E7blCU7nlqN59Vk7e-JBWAtP0-BBtoHOEVbZUy4wDFT4c-IsrypnhRvRqA+5flRUwCVORlQuzOVdVtX9sDjWls1ycdVD0Yw318fbcNKNjRN-JY9BuPzQqBMp6tDb0dP+4gQ59S06+XNUtXFMcCg3DHpeDeUU3LflTbkKeo0hP5MkME-bQfdjSXnlv1LmH5E6lioBAJADEc5iwkmBXSbtMJI2wjAXC+FRjG0YmbAIKJ1z+GwOKDU-E0QwAAOJKg0C7Ze9QGiMJ9v7ewSoQ6tUvmnDOsCm5xwGpUHmMDlqCL6sIxusdhawgwnPPayAcjMJzG5NEGi1BlxJJXfygCKaMjrj-AeACZ73mFJ3cU3cXxQISgPFKqph4iKQqPVQhVirWinkze+ecaq9kXgg+yWCwZr1WtDWG8YVZDXwerfeaNsxH3zCfOapZFoRK6tfDab9Z6hN2o5SB8gDGv1bhTNRYAdGqAxBYu+LMbEShNAgJhSoPR+MwYU+ocAIB9hQOABSkcdE8jESEh4SjSw9L6QMgoQy2naGzkg-xNQXjLF4TmAsDRxjrJQB9aQBZRrhGCIEEEmx4i6hQG6TkVkVjDGWMkUAaormQTMmspUAA5JUakLgwE6DgrBeC1Ya2IUZMYOzVCbO2UqPZByjknOWGci5zzVLaVuWge5CBHnItMqit5cxPlzG+b80hptmL+A4AAdjcE4FATgYgRmCHALiAA2eAE5DA6JgEUeJucVmNFaB0HhfCL6p1kTAaObis5KTBR8l52kYD-MTBIpB9QIlCPFQPco0qdkEpxdZDBBTqpU3RDojEcB2U6L0RXYGZSjGjhgCY+u5jb6RXbtYru7N7G90cZKweLipF-3cRwPKnjx6TywB0lRjkF4GPYZKEVkTN7RPgQC3e8SRpJIxikmaON0n4yyUTG+kbDV7WKcAUpMg8n1GNSgU1OzGZ5KsY-NmwytzFp2tVHZezY3Komd0i1SoBYAQxF26QpIMFxtBaO2FxyFU71VsjIh2roX7PqIc2dJKmLm0sJ-ZymwbZIASGAHdfYID7oAFKoPRZy-wDyQBqm5WrXlkkmjMhkj0HZ-DpGioUnIwNUrRjrExTuqAUzoB7AAOosA+r7HoAAhfiCg4AAGlvjTrXXCud4jxlflVQIn9EcNW+q1YBn4wHKBgagJB6DsGENIdQyCdDMB12BDnQajte0ABWV7TWXvFJalAhJy4+RfpW8p9rHVmN9XU11wDGl2KvA4-uvrnFpQDXAjxXiJ4+IjY2zp88gk9p2k1BNG9epw1ifAdNiTD65lSbNPG58C05IYu2k6nrFMlJtWJu1tc60rpk0AjuHrW3evlKO1T71R2abDTpl1W0o3dkM0vAp-o4MhitVE7elm97phBVmuzObT6lj0OuFEQmcgufi4uRLrS5jP07Lyo1vToBhiQvdcjoGWtQDWKF4Ar1-U-WcrAE0ZoazhhTYU5Z-pAxjba5GJN2XcFpqBfvTMpHMb2dzY50bwZzQwFrArUh3mmYLWwFoE1SoR0rrWNgTrlG1hlpcKFQLi43WP3kvYdFOVIAA3u91mA4o6soAa1N7mKqYAoLQYor803sHzriatpdJDiZkLJV4YA8pYiHrtlATHVngwjewHdwgacuVsNS+7T23tfb+2MDvXtuHej0-Y+5kA3A8Bmo51AK1InGuGJntW7ntTqvM3ewTvAlYWllqejObQaxHjA9B4ORBfbIeoPQUszphlFXKIXQQ5HIxSFAA])
