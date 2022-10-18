package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"sync"
	"time"
)

type Airplane struct {
	name       string
	trapCounts int
}

func (airplane Airplane) goToRandomPlace(wg *sync.WaitGroup) {
	distances := []int{1, 2, 3, 1, 4}
	places := []string{"A", "B", "B", "D", "F"}
	place := rand.Intn(len(distances))
	fmt.Println(airplane.name, "went to", places[place])
	<-time.After(time.Second * time.Duration(distances[place]))
	fmt.Println(airplane.name, "arrived to", places[place])
	defer wg.Done()
}

func arrive(wg *sync.WaitGroup, i int, after int) {
	<-time.After(time.Second * time.Duration(after))
	airplane := Airplane{strconv.FormatInt(int64(i), 10), 2}
	fmt.Println("Airplane", airplane.name, "arrive")
	wg.Add(1)
	go airplane.goToRandomPlace(wg)
	defer wg.Done()
}

func main() {
	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go arrive(&wg, i, rand.Intn(i+1))
	}

	wg.Wait()
}
