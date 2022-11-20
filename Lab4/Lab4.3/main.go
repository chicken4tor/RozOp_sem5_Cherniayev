package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Route struct {
	destination string
	price       int
}

type Graph struct {
	routesList map[string][]*Route
	lock       sync.RWMutex
}

func (graph *Graph) init() {
	graph.routesList = make(map[string][]*Route)
}

func (graph *Graph) addCity(name string) bool {
	if graph.routesList[name] != nil {
		return false
	}
	graph.routesList[name] = make([]*Route, 0)

	return true
}

func (graph *Graph) addRoute(from, to string, price int) bool {
	if price <= 0 {
		price = 1
	}
	_, routeIdx := graph.getRoute(from, to)
	if routeIdx != -1 || graph.routesList[from] == nil || graph.routesList[to] == nil || (from == to) {
		return false
	}
	graph.routesList[from] = append(graph.routesList[from], &Route{to, price})
	graph.routesList[to] = append(graph.routesList[to], &Route{from, price})

	return true
}

func (graph *Graph) removeCity(city string) bool {
	if graph.routesList[city] == nil {
		return false
	}
	for currentCity, _ := range graph.routesList {
		if currentCity == city {
			continue
		}
		graph.removeRoute(currentCity, city)
	}
	delete(graph.routesList, city)

	return true
}

func (graph *Graph) removeRoute(from, to string) bool {
	_, idxInFromAdjList := graph.getRoute(from, to)
	if idxInFromAdjList == -1 {
		return false
	}
	_, idxInToAdjList := graph.getRoute(to, from)
	graph.routesList[from] = removeByIndex(graph.routesList[from], idxInFromAdjList)
	graph.routesList[to] = removeByIndex(graph.routesList[to], idxInToAdjList)

	return true
}

func removeByIndex[T any](slice []T, i int) []T {
	return append(slice[:i], slice[i+1:]...)
}

func (graph *Graph) editRoutePrice(from, to string, newPrice int) bool {
	routeTo, _ := graph.getRoute(from, to)
	if routeTo == nil {
		return false
	}
	routeFrom, _ := graph.getRoute(to, from)
	routeTo.price = newPrice
	routeFrom.price = newPrice

	return true
}

func (graph *Graph) getRoute(from, to string) (*Route, int) {
	if graph.routesList[from] == nil {
		return nil, -1
	}
	var resultRoute *Route = nil
	var resultIdx int = -1
	for i := 0; i < len(graph.routesList[from]); i++ {
		current := graph.routesList[from][i]
		if current.destination == to {
			resultRoute = current
			resultIdx = i
			break
		}
	}

	return resultRoute, resultIdx
}

func getRouteExt(startingNode string, graph *Graph, cities []string, goalNode string) int {
	visited := make(map[string]bool, len(graph.routesList))
	for key := range graph.routesList {
		visited[key] = false
	}

	return dfsRecursive(startingNode, graph, visited, cities, goalNode)
}

func dfsRecursive(startingNode string, graph *Graph, visited map[string]bool, cities []string, goalNode string) int {
	var price = 0
	visited[startingNode] = true
	for _, node := range graph.routesList[startingNode] {
		if !visited[node.destination] {
			if visited[goalNode] == true {
				break
			}
			price = dfsRecursive(node.destination, graph, visited, cities, goalNode)
			price += node.price
		}
	}
	if visited[goalNode] {
		return price
	} else {
		return 0
	}
}

func priceEditor(graph *Graph, cities []string) {
	from := cities[rand.Int()%len(cities)]
	to := cities[rand.Int()%len(cities)]
	if from == to {
		fmt.Printf("PriceEditor: не існує шляху між одним й тим самим містом (%s)\n", from)
		return
	}
	route, _ := graph.getRoute(from, to)
	if route == nil {
		if graph.routesList[from] == nil || graph.routesList[to] == nil {
			fmt.Printf("PriceEditor: одного з міст не існує\n")
		} else {
			fmt.Printf("PriceEditor: не існує прямого шляху з %s до %s\n", from, to)
		}
	} else {
		oldPrice := route.price
		newPrice := rand.Intn(13) + 1
		graph.editRoutePrice(from, to, newPrice)
		fmt.Printf("PriceEditor: змінив ціну з %s до %s. Стара ціна %d, нова ціна %d\n", from, to, oldPrice, newPrice)
	}
}
func routeEditor(graph *Graph, cities []string) {
	from := cities[rand.Int()%len(cities)]
	to := cities[rand.Int()%len(cities)]
	toRemove := rand.Intn(2) == 0
	if toRemove {
		if graph.removeRoute(from, to) {
			fmt.Printf("RouteEditor: видалив шлях з %s до %s\n", from, to)
		} else {
			fmt.Printf("RouteEditor: не існує прямого шляху з %s до %s\n", from, to)
		}
	} else {
		price := rand.Intn(13) + 1
		if graph.addRoute(from, to, price) {
			fmt.Printf("RouteEditor: додав шлях з %s до %s, його ціна: %d\n", from, to, price)
		} else {
			route, _ := graph.getRoute(from, to)

			if route != nil {
				fmt.Printf("RouteEditor: шлях з %s до %s вже існує\n", from, to)
			} else if graph.routesList[from] == nil || graph.routesList[to] == nil {
				fmt.Printf("RouteEditor: одного з міст не існує\n")
			}
		}
	}
}
func cityEditor(graph *Graph, cities []string) {
	city := cities[rand.Int()%len(cities)]
	toRemove := rand.Intn(2) == 0
	if toRemove {
		if graph.removeCity(city) {
			fmt.Printf("CityEditor: видалив %s\n", city)
		} else {
			fmt.Printf("CityEditor: не зміг видалити %s (міста не існує)\n", city)
		}
	} else {
		if graph.addCity(city) {
			fmt.Printf("CityEditor: додав %s\n", city)
		} else {
			fmt.Printf("CityEditor: %s вже існує\n", city)
		}
	}
}
func routeFinder(graph *Graph, cities []string) {
	from := cities[rand.Int()%len(cities)]
	to := cities[rand.Int()%len(cities)]
	price := getRouteExt(from, graph, cities, to)

	if price != 0 && from != to {
		fmt.Printf("RouteFinder: знайшов шлях з %s до %s, його ціна: %d\n", from, to, price)
	} else if from == to {
		fmt.Printf("RouteFinder: не існує шляху між одним й тим самим містом (%s)\n", from)
	} else {
		fmt.Printf("RouteFinder: не зміг знайти шлях з %s до %s\n", from, to)
	}
}

func pricePerformer(graph *Graph, cities []string) {
	for {
		graph.lock.Lock()
		time.Sleep(1000 * time.Millisecond)
		priceEditor(graph, cities)
		graph.lock.Unlock()
	}
}
func routePerformer(graph *Graph, cities []string) {
	for {
		graph.lock.Lock()
		time.Sleep(1000 * time.Millisecond)
		routeEditor(graph, cities)
		graph.lock.Unlock()
	}
}
func cityPerformer(graph *Graph, cities []string) {
	for {
		graph.lock.Lock()
		time.Sleep(1000 * time.Millisecond)
		cityEditor(graph, cities)
		graph.lock.Unlock()
	}
}
func findPerformer(graph *Graph, cities []string) {
	for {
		graph.lock.RLock()
		time.Sleep(1000 * time.Millisecond)
		routeFinder(graph, cities)
		graph.lock.RUnlock()
	}
}

func main() {
	cities := []string{"Kyiv", "Odesa", "Lviv", "Mariupol", "Vinnytsia", "Zhytomyr", "Dnipro", "Kharkiv", "Kherson", "Simferopol", "City1", "City2", "City3"}
	graph := Graph{}
	graph.init()
	for i := 0; i < len(cities); i++ {
		graph.addCity(cities[i])
	}
	graph.addRoute("Kyiv", "Odesa", 4)
	graph.addRoute("Odesa", "Lviv", 7)
	graph.addRoute("Lviv", "Mariupol", 10)
	graph.addRoute("Kyiv", "Vinnytsia", 9)
	graph.addRoute("Vinnytsia", "Zhytomyr", 2)
	graph.addRoute("Vinnytsia", "Kharkiv", 7)
	graph.addRoute("Kyiv", "Kherson", 6)
	graph.addRoute("Kherson", "Simferopol", 8)
	graph.addRoute("Mariupol", "Dnipro", 13)

	go pricePerformer(&graph, cities)
	go routePerformer(&graph, cities)
	go cityPerformer(&graph, cities)
	go findPerformer(&graph, cities)

	for {
	}
}
