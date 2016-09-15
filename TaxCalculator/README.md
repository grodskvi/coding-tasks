## Sales Tax API

Basic sales tax is applicable at a rate of 10% on all goods, except books, food, and medical products that are exempt. Import duty is an additional sales tax applicable on all imported goods at a rate of 5%, with no exemptions.

When I purchase items I receive a receipt which lists the name of all the items and their price (including tax), finishing with the total cost of the items, and the total amounts of sales taxes paid.

The rounding rules for sales tax are as follows. Given a tax rate of n% and price p the tax is n*p/100 rounded up to the nearest 0.05 amount.

Code a simple RESTful API that computes the sales tax according to the description above for a list of products. The API receives the list of products in a HTTP POST request in json format, computes the sales tax and returns the response in json format.

For example, assuming the RESTful service runs on http://localhost/taxcalculator and that in the current directory there is a file “input1.json” that contains the following data:

```json
[
	{
		"description": "Book",
		"count": 1,
		"unitPrice": 12.49
	},
	{
		"description": "Chocolate Bar",
		"count": 1,
		"unitPrice": 0.85
	},
	{
		"description": "Music CD",
		"count": 1,
		"unitPrice": 14.99
	}
]
```

When running the following command:
```
curl -H "Accept: application/json" -H "Content-type: application/json" -X POST –d @input1.json http://localhost/taxcalculator
```

I should get the following response:
```
{“salesTax”: 1.50}
```

Feel free to modify/extend the example data models above if needed. The code should be “production grade”.

##### Examples in textual form

```
1 book at 12.49
1 music CD at 14.99
1 chocolate bar at 0.85
Sales Taxes: 1.50
```

```
1 imported box of chocolates at 10.00
1 imported bottle of perfume at 47.50
Sales Taxes: 7.65
```

```
1 imported bottle of perfume at 27.99
1 bottle of perfume at 18.99
1 packet of headache pills at 9.75
1 box of imported chocolates at 11.25
Sales Taxes: 6.70
```
