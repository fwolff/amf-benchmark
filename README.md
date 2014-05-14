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
benchmark-graniteds-small-list-objects:
     [java] Reading Java data from file: data/small-list-objects.ser
     [java] Setting up GraniteDS
     [java] Warming up (~20 s)...
     [java] Benchmarking serialization...
     [java] Saving AMF3 data to file: data/small-list-objects-graniteds.amf
     [java] Benchmarking deserialization...
     [java] Writing benchmark result to file: data/small-list-objects-graniteds.csv
     [java] -------------------------------------------------------------------------------
     [java]   Platform                   : GraniteDS
     [java]   Java input file            : data/small-list-objects.ser
     [java]   AMF3 output size           : 351,532 bytes (343.3 kb)
     [java]   Iterations                 : 10,000 times
     [java]   Total serialization time   : 13,811 ms (13.81 s)
     [java]   Total deserialization time : 14,176 ms (14.18 s)
     [java] -------------------------------------------------------------------------------

...

benchmark-blazeds-small-list-objects:
     [java] Reading Java data from file: data/small-list-objects.ser
     [java] Setting up BlazeDS
     [java] Warming up (~20 s)...
     [java] Benchmarking serialization...
     [java] Saving AMF3 data to file: data/small-list-objects-blazeds.amf
     [java] Benchmarking deserialization...
     [java] Writing benchmark result to file: data/small-list-objects-blazeds.csv
     [java] -------------------------------------------------------------------------------
     [java]   Platform                   : BlazeDS
     [java]   Java input file            : data/small-list-objects.ser
     [java]   AMF3 output size           : 351,532 bytes (343.3 kb)
     [java]   Iterations                 : 10,000 times
     [java]   Total serialization time   : 33,341 ms (33.34 s)
     [java]   Total deserialization time : 66,815 ms (66.82 s)
     [java] -------------------------------------------------------------------------------

````

This means that GraniteDS takes 13.81 seconds to serialize 10,000 times a 'small' list of objects (50 objects, each
containing a set of other objects) and 14.18 seconds to deserialize them (10,000 times again). The size of the
serialized data, in AMF3, is also printed, 343.3 kilobytes here.

BlazeDS, on the other hand, needs 33.34 seconds to serialize the exact same data 10,000 times and 66.82 seconds to 
deserialize them (10,000 times again).

You can also read this blog post for more results: http://www.granitedataservices.com/2014/05/14/amf3-benchmark-graniteds-3-1-vs-blazeds-4-0/.
