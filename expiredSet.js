db.getCollection("product").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $set: {
                "expired" : {
                    $cond : [
                        {
                            $lt : [
                                "$endDate",
                                new Date()
                            ]
                        },
                        {
                            $sum : 1
                        },
                        0
                    ]
                },
                "unexpired" : {
                    $cond : [
                        {
                            $lt : [
                                "$endDate",
                                new Date()
                            ]
                        },
                        0,
                        {
                            $sum : 1
                        }
                    ]
                }
            }
        },

        // Stage 2
        {
            $group: {
                _id : null,
                total:{ $sum: 1},
                expired : { $sum: "$expired" },
                unexpired : { $sum: "$unexpired" }
            }
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);