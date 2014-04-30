amf-benchmark
====================

AMF3 (de)serialization benchmarks with GraniteDS 3.1 and BlazeDS 4.0.

How to run the benchmarks
-------------------------

You need Java (1.7+ recommended), Apache Ant (1.8+ recommended) and Git (1.8+ recommended).

First, clone the project:

````bash
$ git clone https://github.com/fwolff/amf-benchmark.git
````

Then, go to the newly created amf-benchmark directory and run ant:

````bash
$ cd amf-benchmark
$ ant
````

How to read the results
-----------------------

The benchmark spawns a new JVM for each test and prints results on the standard output.

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
     [java]   AMF3 serialized size       : 346,488 bytes (338.4 kb)
     [java]   Iterations                 : 10,000 times
     [java]   Total serialization time   : 13,542 ms (13.54 s)
     [java]   Total deserialization time : 14,133 ms (14.13 s)
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
     [java]   AMF3 serialized size       : 346,488 bytes (338.4 kb)
     [java]   Iterations                 : 10,000 times
     [java]   Total serialization time   : 34,072 ms (34.07 s)
     [java]   Total deserialization time : 67,305 ms (67.31 s)
     [java] -------------------------------------------------------------------------------

````

This means that GraniteDS takes 13.54 seconds to serialize 10,000 times a 'small' list of objects (50 objects, each
containing a set of other objects) and 14.13 seconds to deserialize them (10,000 times again). The size of the
serialized data, in AMF3, is also printed, 338.4 kilobytes here.

BlazeDS, on the other hand, needs 34.07 seconds to serialize the exact same data 10,000 times and 67.31 seconds to 
deserialize them (10,000 times again).
