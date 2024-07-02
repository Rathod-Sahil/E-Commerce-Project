db.getCollection("product").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $addFields: {
                sort: {
                            $cond: [
                            {
                                    $eq: ["$color", "Blue"]},0,1]
                            }
            }
        },

        // Stage 2
        {
            $sort: {
               sort : 1
                
            }
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);