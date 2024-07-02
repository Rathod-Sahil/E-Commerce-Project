db.getCollection("product").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $match: { 
                "softDelete" : false, 
                "endDate" : { 
                    "$lt" : { 
                        "$date" : "2024-05-30T20:18:31.984Z"
                        }
                    }
                }
        },

        // Stage 2
        {
            $set: {
                "expired" : true
            }
        },

        // Stage 3
        {
            $group: {
                _id: null,
                "expiredProducts": {$sum:1}
            }
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);