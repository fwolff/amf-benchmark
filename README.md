amf-benchmark
====================

AMF3 (de)serialization benchmarks with GraniteDS 3.1 and BlazeDS 4.0.

Requirements
------------

Java (1.7+ recommended) and Apache Ant (1.8+ recommended)

How to run the benchmarks
-------------------------

Clone the project:

````bash
$ git clone https://github.com/fwolff/amf-benchmark.git
````

Go to the newly created amf-benchmark directory and run ant:

````bash
$ cd amf-benchmark
$ ant
````

How to read the results
-----------------------

The benchmark is spawning a new JVM for each test and printing results on the standard output.

For example, you'll see this kind of report:

````bash
benchmark-graniteds-small-list-objects-file:
     [java] Reading data from file: data/small-list-objects.dat
     [java] Warming up...
     [java] Benchmarking serialization...
     [java] Benchmarking deserialization...
     [java] -------------------------------------------------------------------------------
     [java]   Benchmark class            : BenchmarkGraniteDSAmf
     [java]   Data file                  : data/small-list-objects.dat
     [java]   AMF3 serialized size       : 351,150 bytes (342.9 kb)
     [java]   Iterations                 : 10000
     [java]   Total serialization time   : 13,537 ms (13.54 s)
     [java]   Total deserialization time : 14,298 ms (14.30 s)
     [java] -------------------------------------------------------------------------------

...

benchmark-blazeds-small-list-objects-file:
     [java] Reading data from file: data/small-list-objects.dat
     [java] Warming up...
     [java] Benchmarking serialization...
     [java] Benchmarking deserialization...
     [java] -------------------------------------------------------------------------------
     [java]   Benchmark class            : BenchmarkBlazeDSAmf
     [java]   Data file                  : data/small-list-objects.dat
     [java]   AMF3 serialized size       : 351,151 bytes (342.9 kb)
     [java]   Iterations                 : 10000
     [java]   Total serialization time   : 33,018 ms (33.02 s)
     [java]   Total deserialization time : 67,531 ms (67.53 s)
     [java] -------------------------------------------------------------------------------

````

This means that GraniteDS takes 13.54 seconds to serialize 10.000 times a 'small' list of objects (50 objects, each
containing a set of other objects) and 14.30 seconds to deserialize them (10.000 times again). The size of the
serialized data, in AMF3, is also printed, 342.9 kilobytes here.

BlazeDS, on the other hand, needs 33.02 seconds to serialize the exact same data 10.000 times and 67.53 seconds to 
deserialize them (10.000 times again).
