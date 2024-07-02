db.getCollection("product").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $match: { 
            "softDelete" : false, 
            "establishedDate" : { 
                "$gt" : { 
                    "$date" : "2024-05-30T20:18:31.995Z"
                    }
                }
            }
        },

        // Stage 2
        {
            $count: "comingSoonProducts"
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);