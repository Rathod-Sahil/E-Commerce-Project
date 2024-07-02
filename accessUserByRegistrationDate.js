db.getCollection("students").aggregate(

	// Pipeline
	[
		// Stage 1
		{
			$addFields: {
			    year: { $year: "$registrationDate" },
			    month: { $month: "$registrationDate" },
			    day: { $dayOfMonth: "$registrationDate" }
			}
		},

		// Stage 2
		{
			$set: {
			     quarter: {
			      $cond: {
			        if: { $lte: ["$month", 3] },
			        then: "Q1",
			        else: {
			          $cond: {
			            if: { $lte: ["$month", 6] },
			            then: "Q2",
			            else: {
			              $cond: {
			                if: { $lte: ["$month", 9] },
			                then: "Q3",
			                else: "Q4"
			              }
			            }
			          }
			        }
			      }
			    }
			}
		},

		// Stage 3
		{
			$match: {
			    "day" : 5,
			    "month" : 3,
			    "year" : 2024,
			    "quarter" : "Q1"
			    
			}
		},

	]

	// Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);
