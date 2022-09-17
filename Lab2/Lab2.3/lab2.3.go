package main

import (
	"fmt"
	"math/rand"
	"time"
)

func getWinner(monks [1000]int, start int, end int) int {
	var result int = start
	if monks[result] < monks[end] {
		result = end
	}
	return result
}

func getNameOfMonastery(id int) string {
	var result string
	switch id % 2 {
	case 0:
		result = "Гуань-Інь"
	case 1:
		result = "Гуань-Янь"
	}
	return result
}

func Fight(powers [1000]int, start int, end int, channel chan<- int) {
	var winner int = start
	if end-start < 2 {
		winner = getWinner(powers, start, end)
	} else {
		var addChannel = make(chan int, 2)
		go Fight(powers, start, (start+end)/2, addChannel)
		Fight(powers, ((start+end)/2)+1, end, addChannel)
		var firstWinner = <-addChannel
		var secondWinner = <-addChannel
		winner = getWinner(powers, firstWinner, secondWinner)
	}
	channel <- winner
}

func main() {
	start := time.Now()

	var monksEnergy [1000]int
	for i := 0; i < 1000; i++ {
		monksEnergy[i] = rand.Int() % 10000
	}

	var channel = make(chan int, 1)
	Fight(monksEnergy, 0, len(monksEnergy)-1, channel)
	var winnerID = <-channel

	duration := time.Since(start)

	fmt.Println("ID:", winnerID)
	fmt.Println("Енергія:", monksEnergy[winnerID], "Ци")
	fmt.Println("Монастир:", getNameOfMonastery(winnerID))

	fmt.Println(duration)

}
