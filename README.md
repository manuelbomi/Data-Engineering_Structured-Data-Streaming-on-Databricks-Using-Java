# Data Engineering: Structured Data Streaming on Databricks Using Java

### Brief Overview
Databricks leverages Apache Spark's Structured Streaming for stream processing, and while Scala and Python are commonly used, Java can also be employed. Here's an example demonstrating a basic stream processing application in Java on Databricks:


#### Define Your Data Source (e.g., a Delta Table) 
---
```ruby
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.DataTypes;

public class SimpleStreamProcessor {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession
            .builder()
            .appName("SimpleStreamProcessor")
            .getOrCreate();

        // Define schema for your streaming data
        StructType schema = DataTypes.createStructType(new org.apache.spark.sql.types.StructField[] {
            DataTypes.createStructField("id", DataTypes.IntegerType, true),
            DataTypes.createStructField("value", DataTypes.StringType, true),
            DataTypes.createStructField("timestamp", DataTypes.TimestampType, true)
        });

        // Read streaming data from a Delta Lake table
        Dataset<Row> streamingDf = spark
            .readStream()
            .format("delta")
            .schema(schema) // Apply the defined schema
            .load("/mnt/delta_tables/my_streaming_source"); // Replace with your Delta table path

```
---

#### Apply Transformations (e.g., filtering, aggregation)
---
```ruby
       // Example transformation: filter out rows where 'id' is less than 10
        Dataset<Row> transformedDf = streamingDf.filter("id >= 10");

        // Example transformation: count occurrences of 'value'
        // Dataset<Row> aggregatedDf = streamingDf.groupBy("value").count();
```
---

####   Write the Stream to a Sink (e.g., another Delta Table, Console)
---
```ruby
          // Write the processed data to a new Delta Lake table
        StreamingQuery query = transformedDf
            .writeStream()
            .format("delta")
            .outputMode("append") // Or "complete", "update" depending on your needs
            .option("checkpointLocation", "/mnt/checkpoints/simple_stream_processor") // Important for fault tolerance
            .start("/mnt/delta_tables/my_processed_sink"); // Replace with your sink Delta table path

        // Alternatively, write to console for testing
        // StreamingQuery query = transformedDf
        //     .writeStream()
        //     .format("console")
        //     .outputMode("append")
        //     .start();

        query.awaitTermination(); // Keep the query running until terminated
    }
}
```
#### Job Deployment on Databricks

To deploy on Databricks:
---
    • Create a JAR: Compile your Java code into a JAR file.
    ---
    • Upload to Databricks: Upload the JAR to your Databricks workspace.
    ---
    • Create a Job: Configure a Databricks job, selecting your uploaded JAR and specifying the main class (SimpleStreamProcessor in this example).
    ---
    • Attach to Cluster: Attach the job to a suitable Databricks cluster for execution.


#### Term Definition 
    • SparkSession: The entry point for interacting with Spark.
    ---
    • readStream().format("delta").load(...): Reads data from a Delta Lake table as a streaming source. You can also read from other 
    sources like Kafka or cloud storage.
    ---
    • schema(...): Defines the structure of your incoming streaming data.
    ---
    • filter(...): Applies a transformation to filter data based on a condition.
    ---
    • writeStream().format("delta").outputMode(...).option("checkpointLocation", ...).start(...): Writes the processed stream to 
    a Delta Lake table.
        ◦ outputMode: Determines how the output is written (e.g., append for new rows, complete for full results, update for changes).
        ◦ checkpointLocation: Crucial for fault tolerance, storing information about the processed data to enable recovery from failures.
        ---
    • awaitTermination(): Keeps the streaming query running indefinitely until explicitly stopped or an error occurs.
---
### Author's Background
---

```
> [!NOTE]
Author's Name:  Emmanuel Oyekanlu
Skillset:   I have experience spanning several years in developing scalable enterprise data pipelines,
solution architecture, architecting enterprise data and AI solutions, deep learning and LLM applications as
well as deploying solutions (apps) on-prem and in the cloud.

I can be reached through: manuelbomi@yahoo.com
Website:  http://emmanueloyekanlu.com/
Publications:  https://scholar.google.com/citations?user=S-jTMfkAAAAJ&hl=en
LinkedIn:  https://www.linkedin.com/in/emmanuel-oyekanlu-6ba98616
Github:  https://github.com/manuelbomi

```

[![Icons](https://skillicons.dev/icons?i=aws,azure,gcp,scala,mongodb,redis,cassandra,kafka,anaconda,matlab,nodejs,django,py,c,anaconda,git,github,mysql,docker,kubernetes&theme=dark)](https://skillicons.dev)







