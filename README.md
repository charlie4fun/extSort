# External Sorting
Generates and sort data while being limited in memory (-Xmx500M)

### Build and Run
```sh
mvn clean compile assembly:single
java -jar target/ext-sorting-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Details
1. Data is generated by Produces class, currently in generates only long values.
2. Then data is splited into files, which size depends on JVM memory limitations, by BatchSorter. It also sorts data with Collections.sort().
3. Then files are joined by SortedLongsFilesMerger.

Tests shows that with -Xmx500M size of Batch files from step two could be up to 230 MB. And SortedLongsFilesMerger could handle up to 5900 files. So size of initial usorted file that could be handled is up to 1300 GB (text data).

### Todos
 - Write MORE Tests
 - Add Night Mode
