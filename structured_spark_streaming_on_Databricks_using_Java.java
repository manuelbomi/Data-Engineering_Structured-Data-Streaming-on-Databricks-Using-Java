//Define Your Data Source (e.g., a Delta Table)
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

// Apply Transformations (e.g., filtering, aggregation):

        // Example transformation: filter out rows where 'id' is less than 10
        Dataset<Row> transformedDf = streamingDf.filter("id >= 10");

        // Example transformation: count occurrences of 'value'
        // Dataset<Row> aggregatedDf = streamingDf.groupBy("value").count();

// Write the Stream to a Sink (e.g., another Delta Table, Console):

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
