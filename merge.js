db.getCollection("col1").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $merge: {
                 
                 into: "col2",
                 whenMatched: "merge"
            }
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);