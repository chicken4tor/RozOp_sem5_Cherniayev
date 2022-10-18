package main

import (
	"fmt"
	"sync"
	"time"
)

type node struct {
	from  string
	to    string
	price int
}
type city struct {
	price    int
	cityName string
}

var cities = make(map[string][]city)

var mutex sync.RWMutex

func deleteCity(town string) {
	for i := 0; i < len(cities[town]); i++ {
		dest := cities[town][i].cityName

		for j := 0; j < len(cities[dest]); j++ {
			if cities[dest][j].cityName == town {
				if j == 0 {
					cities[dest] = cities[dest][1:]
				} else {
					cities[dest] = append(cities[dest][:j], cities[dest][j+1:]...)
				}
				break
			}

		}

	}
	delete(cities, town)
}
func addCity(town string) {
	if _, ok := cities[town]; !ok {
		cities[town] = []city{}
	}
}
func deleteRoute(From string, To string) {
	for i := 0; i < len(cities[From]); i++ {
		if cities[From][i].cityName == To {
			cities[From] = append(cities[From][:i], cities[From][i+1:]...)
			break
		}
	}
	for i := 0; i < len(cities[To]); i++ {
		if cities[To][i].cityName == From {
			cities[To] = append(cities[To][:i], cities[To][i+1:]...)
			break
		}
	}

}
func addRoute(From string, To string, price int) {

	cities[From] = append(cities[From], city{price, To})
	cities[To] = append(cities[To], city{price, From})

}
func addDeleteWrapper() {
	work := []string{"D", "E", "F", "K", "M", "N"}

	for i := 0; i < len(work); i++ {
		mutex.Lock()

		addCity(work[i])
		fmt.Printf("ADDED CITY. CITY: %s \n", work[i])
		mutex.Unlock()
		printCities()
		time.Sleep(7000 * time.Millisecond)
	}
	for i := len(work) - 1; i >= 0; i-- {
		mutex.Lock()

		deleteCity(work[i])
		fmt.Printf("DELETED CITY. CITY: %s \n", work[i])
		mutex.Unlock()
		printCities()
		time.Sleep(7000 * time.Millisecond)
	}
}
func addDeleteRouteWrapper() {
	work := []node{{"A", "F", 22}, {"F", "D", 2}, {"A", "S", 9}, {"B", "D", 28}}
	for i := 0; i < len(work); i++ {
		mutex.Lock()

		addRoute(work[i].to, work[i].from, work[i].price)

		fmt.Printf("ADDED ROUTE. FROM: %s , TO: %s , PRICE: %d \n", work[i].to, work[i].from, work[i].price)
		mutex.Unlock()
		printCities()
		time.Sleep(7000 * time.Millisecond)
	}
	for i := len(work) - 1; i >= 0; i-- {
		mutex.Lock()
		deleteRoute(work[i].to, work[i].from)
		fmt.Printf("DELETED ROUTE. FROM: %s , TO: %s , PRICE: %d \n", work[i].to, work[i].from, work[i].price)
		mutex.Unlock()
		printCities()
		time.Sleep(7000 * time.Millisecond)
	}
}
func printCities() {
	mutex.RLock()
	fmt.Printf("------------------------------\n")
	for key := range cities {
		size := len(cities[key])
		for i := 0; i < size; i++ {
			fmt.Printf("FROM: %s , TO: %s , PRICE: %d\n", key, cities[key][i].cityName,
				cities[key][i].price)
		}
		if size == 0 {
			fmt.Printf("CITY(NO ROUTES): %s \n", key)
		}
	}
	fmt.Printf("------------------------------\n")
	mutex.RUnlock()
}
func changePrice(from string, to string, price int) {
	size := len(cities[from])
	for i := 0; i < size; i++ {
		if cities[from][i].cityName == to {
			cities[from][i].price = price
		}
	}
	size = len(cities[to])
	for i := 0; i < size; i++ {
		if cities[to][i].cityName == from {
			cities[to][i].price = price
		}
	}
}

func changePriceWrapper() {
	work := []node{{"A", "B", 19}, {"D", "C", 55}, {"A", "B", 20},
		{"A", "D", 111}}
	for i := 0; i < len(work); i++ {

		mutex.Lock()

		changePrice(work[i].from, work[i].to, work[i].price)
		fmt.Printf("Changed price. From:%s , To: %s, NEW PRICE: %d\n", work[i].from, work[i].to, work[i].price)
		mutex.Unlock()
		printCities()
		time.Sleep(5000 * time.Millisecond)
	}
}
func findPath(From string, To string) int {
	if _, ok := cities[From]; !ok {
		return -1
	}

	var sum = -1

	isVisited := make(map[string]int)
	prices := make(map[string]int)
	prices[From] = 0
	stack := []string{From}

	for len(stack) != 0 {
		curr := stack[len(stack)-1]
		stack = stack[:len(stack)-1]
		if _, ok := isVisited[curr]; ok {
			continue
		}
		sum = prices[curr]
		if curr == To {
			return sum
		}
		isVisited[curr] = sum

		for i := 0; i < len(cities[curr]); i++ {
			if _, ok := isVisited[cities[curr][i].cityName]; !ok {
				stack = append(stack, cities[curr][i].cityName)
				prices[cities[curr][i].cityName] = sum + cities[curr][i].price

			}
		}

	}

	return -1
}
func findPathWrapper() {
	work := []node{{"A", "C", -1}, {"B", "F", -1}, {"B", "C", -1},
		{"E", "D", -1}}
	for i := 0; i < len(work); i++ {
		mutex.RLock()
		fmt.Printf("Looking for path FROM: %s , TO: %s \n", work[i].from, work[i].to)
		res := findPath(work[i].from, work[i].to)
		if res == -1 {
			fmt.Printf("There is no path FROM: %s , TO: %s \n", work[i].from, work[i].to)
		} else {
			fmt.Printf("RESULT= %d \n", res)
		}

		time.Sleep(4000 * time.Millisecond)
		mutex.RUnlock()
	}
}

func main() {

	cities["A"] = []city{{10, "B"}, {8, "D"}}
	cities["B"] = []city{{10, "A"}, {3, "D"}}
	cities["C"] = []city{{12, "D"}}
	cities["D"] = []city{{8, "A"}, {3, "B"}, {12, "C"}}

	go changePriceWrapper()
	go addDeleteRouteWrapper()
	go addDeleteWrapper()
	go findPathWrapper()
	for {
	}
}
