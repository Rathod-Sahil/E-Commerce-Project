db.getCollection("col1").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $out: "col3"
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);