db.getCollection("product").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $match: { 
            "softDelete" : false, 
            "establishedDate" : {
                 "$lte" : {"$date" : "2024-05-30T20:18:31.893Z"}}, 
                      "$and" : [{
                           "endDate" : {
                                "$gte" : { "$date" : "2024-05-30T20:18:31.893Z"}}}]}
        },

        // Stage 2
        {
            $group: {
                _id: null,
                availableProducts: {$count:{}}
            }
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);