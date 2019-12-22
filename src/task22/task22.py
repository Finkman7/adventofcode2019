N = 10007

deck = [x for x in range(N)]

def shuffle(deck):
    for line in open('input22.txt'):
        commands = line.split(' ')
        if commands[0] == 'cut':
            n =  int(commands[-1])
            deck = deck[n:] + deck[:n]
        elif commands[1] == 'into':
            deck.reverse()
        elif commands[1] == 'with':
            n =  int(commands[-1])
            deck_ = list(deck)
            for i in range(len(deck)):
                deck_[(i*n) % len(deck_)] = deck[i]
            deck = deck_
    return deck

for i in range(101741582076661):
    deck = shuffle(deck)
print("part 2: " + str(deck[2020]))
#print([1 for x in deck if x == 1])
#print(deck.index(2019))