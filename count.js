db.getCollection("product").aggregate(

    // Pipeline
    [
        // Stage 1
        {
            $set: { 
                "availableProducts" :{ 
                    $count : {
                  $match: {
                  $and: [
                    { establishedDate: { $lte: new Date() } }, // Established date less than or equal to today
                    { endDate: { $gte: new Date() } } // End date greater than or equal to today (available)
                  ]
                }
              }
             },
             "comingsonnProducts" :{ $count : {
                  $match: {
                        establishedDate: { $gt: new Date() } // Established date greater than today (coming soon)
                }
              }
             }
            }
        }
    ],

    // Options
    {

    }

    // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);