//Черняєв Андрій ІПС-32

package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var semaphore sync.Mutex

var table = []int{0, 0, 0}

func mediator() {
	for {
		if table[0] == 0 && table[1] == 0 && table[2] == 0 {
			semaphore.Lock()
			var opt = rand.Intn(3)
			if opt == 0 {
				table[0] = 1
				table[1] = 1
				fmt.Println("Поклав на стіл тютюн та папір")
			} else if opt == 1 {
				table[0] = 1
				table[2] = 1
				fmt.Println("Поклав на стіл тютюн та сірники")
			} else {
				table[1] = 1
				table[2] = 1
				fmt.Println("Поклав на стіл папір та сірники")
			}
			time.Sleep(1200 * time.Millisecond)
			semaphore.Unlock()
		}
	}
}

func smoker(idOfItem int) {
	for {
		if table[0] != 0 || table[1] != 0 || table[2] != 0 {
			semaphore.Lock()
			if table[idOfItem] == 0 {
				if idOfItem == 0 {
					fmt.Println("Курець №1 додав тютюн, скрутив цигарку і покурив")
				} else if idOfItem == 1 {
					fmt.Println("Курець №2 додав папір, скрутив цигарку і покурив")
				} else {
					fmt.Println("Курець №3 додав сірники, скрутив цигарку і покурив")
				}
				time.Sleep(1200 * time.Millisecond)
				table[0] = 0
				table[1] = 0
				table[2] = 0
			}
			semaphore.Unlock()
		}
	}
}

func main() {

	go mediator()

	go smoker(0)
	go smoker(1)
	go smoker(2)

	for {
	}
}
