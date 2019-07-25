# Servirtium

![](Servirtium.svg?raw=true&sanitize=true)

Servirtium == Service Virtualized HTTP (for Java) in a record/playback style

Utilization of "Service Virtualization" is best practice towards fast and 
consistent test automation. This tech should be used in conjunction with 
JUnit/TestNG, etc.  Versus alternate technologies, Servirtium utilizes Markdown
for recorded HTTP conversations, which aids readability and diffing. Those are
helpful when Service Virtualization is participating in a **Technology Compatibility Kit**

## Design goals 

1. By being a "man in the middle" it enables the recording of HTTP conversations and store them in Markdown under 
source-control co-located with the automated tests themselves. 
2. In playback, Servirtium allows the functionality tested in the service tests to be isolated from potentially flaky 
and unquestionably slower "down stack" and remote services.
3. A diffable format to clearly show the differences between two recordings of the same conversation.

## Design Limitations

And this is just for Java teams. Non-Markdown alternates are: 

* [Mountebank](http://mbtest.org) by ThoughtWorker Brandon Byars for a more versatile SV solution (written in NodeJs, but usable 
from other languages).
* [WireMock](http://wiremock.org/) (more establised)
* Netflix's [Polly.js](https://github.com/Netflix/pollyjs/) (new in 2019)
* Linkedin's [Flashback](https://github.com/linkedin/flashback) (since 2017)

Not only is Servirtium just for Java teams wanting it is **in the same process** as the test-runner. It is not designed to be a 
standalone server, although it cn be used that way.

## What do recordings look like?

See [ExampleSubversionCheckoutRecording.md](https://github.com/paul-hammant/servirtium/blob/master/src/test/resources/ExampleSubversionCheckoutRecording.md) 
which was recorded from a real Subversion 'svn' command line client doing it's thing, but 
thru Servirtium as a HTTP-proxy. After the recording of that, the replay side of Servirtium was able 
to pretend to be Apache+Subversion for a fresh 'svn checkout' command. 
[This one](https://github.com/paul-hammant/servirtium/blob/master/src/test/java/com/paulhammant/servirtium/SubversionCheckoutRecorderMain.java) 
was the recorder, and [this one](https://github.com/paul-hammant/servirtium/blob/master/src/test/java/com/paulhammant/servirtium/SubversionCheckoutReplayerMain.java) 
the replayer for that recorded conversation.

## Implementation Limitations

1. Java only for now, though usable in the broader JVM ecosystem. Ports to other languages 
is a direction I'd like to go in. Perhaps a rewrite in Rust, and then bindings back to Java, C#, 
Python, Ruby and NodeJs would be a more sustainable route long term.

2. The recorder **isn't very good at handling parallel requests**. Most of the 
things you want to test will be serial (and  short) but if your client is a browser, 
then you should half expect for parallelized operations.

3. Servirtium can't yet listen on over HTTPS.

4. Servirtium can't yet function as a HTTP Proxy server. It must be a "man in the middle", 
meaning you have to be able to override the endpoints of services during JUnit/TestNG invocation 
in order to be able to record them (and play them back).
 
5. Some server technologies (like Amazon S3) sign payloads in a way that breaks for middle-man 
deployments. See [S3](https://github.com/paul-hammant/servirtium/wiki/S3).
 
# Notable examples of use

## SvnMerkleizer project - emulation of Subversion

[Read more about two seprate uses of Servirtium for this project](docs/SvnMerkleizer_More_Info.md)

## Climate API demo

The World Bank's Climate Data service turned into a Java library: https://github.com/paul-hammant/climate-data-tck

## Todobackend record and playback

[TodobackendDotComServiceRecording.md](https://github.com/paul-hammant/servirtium/blob/master/src/test/resources/TodobackendDotComServiceRecording.md) 
is a recording of the Mocha test site of "TodoBackend.com" against a real Ruby/Sinatra/Heroku 
endpoint. This is not an example of something you'd orchestrate in Java/JUnit, but it is 
an example of a sophisticated series of interactions over HTTP between a client (the browser) 
and that Heroku server. Indeed, the intent of the site is show that multiple backends should be
compatible with that JavaScript/Browser test suite.

[Here's the code for the recorder](https://github.com/paul-hammant/servirtium/blob/master/src/test/java/com/paulhammant/servirtium/SubversionCheckoutRecorderMain.java) 
of that, and [here's the code for the replayer](https://github.com/paul-hammant/servirtium/blob/master/src/test/java/com/paulhammant/servirtium/SubversionCheckoutReplayerMain.java)
for that.  

Note: playback does not pass all the tests because there's a randomized GUID in the request 
payload that changes every time you run the test suite. It gets one third of the way through though.

**Note: this limitation is being resolved, presently**

## Readiness for general industry by lovers of test automation?

Nearly ready, but still being actively worked on.

## Servirtium's default listening port

As per [the default port calculator](https://paul-hammant.github.io/default-port-calculator/#servirtium)  for 'servirtium': 61417 

# Building Servirtium

This builds the binaries, but skips integration tests as they rely on Wikipedia, Reddit 
and others which are moving targets sometimes.

```
mvn clean install
```

This builds the binaries, and includes integration tests (that use various services on the web)

```
mvn clean install -Ptests
```

## License & Legal warning

BSD 2-Clause license (open source)

Be careful: your contracts and EULAs with service providers 
(as well as application/server makers for on-premises) might not allow you to 
reverse engineer their over-the-wire APIs.  

A real case: [Reverse engineering of competitor’s software cost company big](http://blog.internetcases.com/2017/10/24/reverse-engineering-of-competitors-software-cost-company-big/) - and you might say that such clauses are needed to prevent licensees from competing with the original company with arguably "stolen" IP. 

We (test engineers) might morally think that we should be OK for this, as we're just doing it for 
test-automation purposes. No matter, the contracts that are signed often make no such distinction, but 
the case above was where the original maker of an API went after a company that was trying to make 
something for the same ecosystem without a commercial relation on that specifically.

## Code of Conduct

Be Nice