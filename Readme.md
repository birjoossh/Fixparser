90% of storage used … If you run out, you can't create, edit, and upload files. Get 100 GB of storage for HK$15.00 HK$0
for 1 month.
Abc.txt

# FixParser Library

## Project Overview

FixParser is a Java library designed to parse FIX (Financial Information eXchange) messages. It provides both
synchronous and asynchronous parsing capabilities to handle FIX messages efficiently.

## Usage

### Synchronous Parsing

The `parseFixMessage` method parses a FIX message byte array synchronously. It takes a FIX message byte array and a
boolean flag to indicate whether the message should be validated.

#### Example

```java
byte[] fixMessageBytes = "8=FIX.4.2 9=142 35=D 11=123 109=ultrarichclient 76=jp 1=clientacc "
        + "78=2 79=acc1 80=100 79=acc2 80=200 55=appl 54=1 38=300 "
        + "44=1.67 40=1 10=21 ".getBytes();
FixMessage fixMessage = FixMessageParser.parseFixMessage(fixMessageBytes, false);
System.out.

println(fixMessage);
```

### Asynchronous Parsing

The parseFixMessageAsynchronous method parses a FIX message byte array asynchronously using a CompletableFuture. It
takes a FIX message byte array and a boolean flag to indicate whether the message should be validated.

#### Example

```java
FixMessageParser parser = new FixMessageParser();
byte[] fixMessageBytes = "8=FIX.4.2 9=142 35=D 11=123 109=ultrarichclient 76=jp 1=clientacc "
        + "78=2 79=acc1 80=100 79=acc2 80=200 55=appl 54=1 38=300 "
        + "44=1.67 40=1 10=21 ".getBytes();
CompletableFuture<FixMessage> future = parser.parseFixMessageAsynchronous(fixMessageBytes, false);
future.

thenAccept(fixMessage ->System.out.

println(fixMessage));
        parser.

shutdown();
```

### Building the Project

To build the FixParser project, you need to use Maven. Follow the steps below to build the project:
Open a terminal or command prompt.
Navigate to the root directory of the project where the pom.xml file is located.
Run the following command to build the project:

```bash
mvn clean install
```

### Performance Benchmarking

#### Overview

The library includes methods to benchmark the performance of synchronous and asynchronous parsing. The benchmarking
results can help in understanding the performance characteristics of the parsing methods under different loads.  
Please refer to the [PerformanceBenchmark](src/main/java/com/fixparser/performance/PerformanceBenchmark.java) class for
more details.

#### Running the Benchmark

1. Set the JAVA_HOME environment variable to your Java installation path.
2. Use the java command from the JAVA_HOME path to execute the JAR file with the required arguments.

##### Command

```bash
set JAVA_HOME=<java home path>
$JAVA_HOME/bin/java -jar target\FixParser-1.0-SNAPSHOT.jar <maxLoadFactor> <runbenchmarkParsingInParallel> <benchmarkParsingInSequence>
```

##### Example

```bash
$JAVA_HOME/bin/java -jar target\FixParser-1.0-SNAPSHOT.jar 5 true true
```