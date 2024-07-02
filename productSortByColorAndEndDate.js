db.getCollection("product").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $addFields: {
                sort: {
                            $cond: {
                                if: {
                                    $eq: ["$color", "Blue"]
                                },
                                then: 0,
                                else: 1
                            }
                        }
            }
        },

        // Stage 2
        {
            $sort: {
               sort : 1,
               endDate: -1
                
            }
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);