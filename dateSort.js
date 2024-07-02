db.getCollection("collection4").aggregate(

	// Pipeline
	[
		// Stage 1
		{
			$project: {
			      originalDate: "$date",
			      month:{ $substr: [ "$date", 0, 2 ] },
			      day:  { $substr: [ "$date", 2, 2 ] },
			      year: { $substr: [ "$date", 4, 2 ] }
			}
		},

		// Stage 2
		{
			$sort: {
			    year: 1,
			    month: 1,
			    day: 1
			    
			}
		},

		// Stage 3
		{
			$project: {
			    "originalDate" : 1
			}
		},

	]

	// Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);
