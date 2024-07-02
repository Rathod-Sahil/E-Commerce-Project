// Stages that have been excluded from the aggregation pipeline query
__3tsoftwarelabs_disabled_aggregation_stages = [

	{
		// Stage 2 - excluded
		stage: 2,  source: {
			$set: {
			   "productDetails.expired" : {
			        $cond : [{ $lt : ["$productDetails.endDate" ,new Date()]}, true , false]
			        }
			}
		}
	},
]

db.getCollection("user").aggregate(

	// Pipeline
	[
		// Stage 1
		{
			$lookup: // Equality Match
			{
			    from: "product",
			    let: {"products" : "$productList"},
			    pipeline:[{
			        $match : {
			            $expr:{
			                    $in: [{$toString : "$_id"},"$$products"]
			            },
			            "softDelete" : false,
			            "discountRate" : {$gt : 0}
			        }
			    }],
			    as: "productDetails",
			}
			
			// Uncorrelated Subqueries
			// (supported as of MongoDB 3.6)
			// {
			//    from: "<collection to join>",
			//    let: { <var_1>: <expression>, …, <var_n>: <expression> },
			//    pipeline: [ <pipeline to execute on the collection to join> ],
			//    as: "<output array field>"
			// }
			
			// Correlated Subqueries
			// (supported as of MongoDB 5.0)
			// {
			//    from: "<foreign collection>",
			//    localField: "<field from local collection's documents>",
			//    foreignField: "<field from foreign collection's documents>",
			//    let: { <var_1>: <expression>, …, <var_n>: <expression> },
			//    pipeline: [ <pipeline to run> ],
			//    as: "<output array field>"
			// }
		},

		// Stage 3
		{
			$set: {
			    "productDetails.expired" : {
			        $filter : {
			            input : "$productDetails",
			            as : "item",
			            cond : {
			                            $lt : [
			                                "$$item.endDate",
			                                new Date()
			                            ]
			                        }
			        }
			    }
			}//{
			//    "productDetails.expired":true
			//}
		},

	]

	// Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);
