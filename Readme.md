# FixParser Library

## Project Overview

FixParser is a Java library designed to parse FIX (Financial Information eXchange) messages. It provides api for both
synchronous and asynchronous parsing of the FIX messages.
The code is structured to extend to any object encoded in FIX format by overriding the Fix parser method for that specific object.
Correct object specific parsers are loaded for the message parsing depending on the message type.
Current code base only support NewSingleOrder object message parsing. 

## Usage
### Synchronous Parsing
The `parseFixMessage` method parses a FIX message byte array synchronously. It takes a FIX message byte array and a
boolean flag to indicate whether the message should be validated. 
For performance reason, a user might want to disable message validation by passing appropriate boolean flag.

#### Example
```java
byte[] fixMessageBytes = "8=FIX.4.2 9=142 35=D 11=123 109=ultrarichclient 76=jp 1=clientacc "
        + "78=2 79=acc1 80=100 79=acc2 80=200 55=appl 54=1 38=300 "
        + "44=1.67 40=1 10=21 ".getBytes();
FixMessage fixMessage = FixMessageParser.parseFixMessage(fixMessageBytes, false);
System.out.println(fixMessage);
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
future.thenAccept(fixMessage ->System.out.println(fixMessage));
parser.shutdown();
```

### Building the Project

To build the FixParser project, you need to use Maven. Follow the steps below to build the project:
1. Open a terminal or command prompt.
2. Navigate to the root directory of the project where the pom.xml file is located.
3. Run the following command to build the project:

```bash
mvn clean install
```

### Performance Benchmarking

#### Overview

The library includes methods to benchmark the performance of synchronous and asynchronous parsing. The benchmarking
results can help in understanding the performance characteristics of the parsing methods under different loads.  
Please refer to the [PerformanceBenchmark](src/main/java/com/fixparser/performance/PerformanceBenchmark.java) class for more details.

#### Running the Benchmark

1. Set the JAVA_HOME environment variable to your Java installation path.
2. Use the java command from the JAVA_HOME path to execute the JAR file with the required arguments.

##### Command

```bash
set JAVA_HOME=<java home path>
$JAVA_HOME/bin/java -jar target\FixParser-1.0-SNAPSHOT.jar <maxLoadFactor> <benchmarkParsingInParallel> <benchmarkParsingInSequence>
```

##### Example

```bash
$JAVA_HOME/bin/java -jar target\FixParser-1.0-SNAPSHOT.jar 7 true true
```
On my windows system with 16GB RAM and 5 core cpu, we see significant improvement with async api then the sync api when the load is high enough. 
For lower loads, the thread creation overhead is too much to bring any improvement. 
Below is the tabulation of the results under varying workflow. 

### Benchmarking with multi-threading :
| Total Load  |Average execution time(nanoseconds)|Total execution time(nanoseconds)|
|-------------|-----------------------------------|---------------------------------|
|1|153567500.00|153567500.00|
|10|1151760.00|11517600.00|
|100|1714714.00|171471400.00|
|1000|234910.30|234910300.00|
|10000|103215.34|1032153400.00|
|100000|22862.61|2286260500.00|
|1000000|28902.03|28902031900.00|


### Benchmarking with single-threading :
| Total Load |Average execution time(nanoseconds)|Total execution time(nanoseconds)|
|------------|-----------------------------------|---------------------------------|
|1|104100.00|104100.00|
|10|104820.00|1048200.00|
|100|57421.00|5742100.00|
|1000|72515.10|72515100.00|
|10000|79676.60|796766000.00|
|100000|51259.51|5125951400.00|
|1000000|47282.91|47282907500.00|

