A plugin which allows players to create ticket shop guis for purchasing tickets from the Traincarts plugin.

## How to use
- Run `/train ticket create <ticket name>` to create a Traincarts ticket.
- Run `/tshop ticket create <ticket name> <display name of ticket to use>` to get a "gui ticket" item.
- Run `/tshop gui create <Gui ID> <display name>` to create a shop GUI.
- Run `/tshop gui edit <Gui ID>`
- Drag the ticket you were given by the plugin into the inventory that opens.
 ![image](https://github.com/DNAmaster10/TraincartsTicketShop/assets/67452089/20acf26a-fb5d-461c-bbb6-c840e0ecd0b9)
- Run `/tshop gui open <Gui ID>` to open the shop GUI. Click on a ticket to get it in your inventory.

## Other methods of opening the GUI
### Citizens NPCs (coming soon)
### Entities (coming soon)
### Signs
Create a sign with this format:
```

[tshop]
<gui ID>

```
![image](https://github.com/DNAmaster10/TraincartsTicketShop/assets/67452089/8a287820-3aab-4e71-ae9a-40ebfc168b81)

Right click on it to open the shop GUI.

## Links
Links are a powerful tool which allows players to "link" multiple guis together.

To get started, you must have at least two guis already created. For example, we may have run:
- `/tshop gui create mainline Mainline Tickets`
- `/tshop gui create subline Subline Tickets`

We may want to have it so that the gui `mainline` links to the second gui, `subline`.

Firstly, we must create a new "link". To do this, we can run:
- `/tshop link create <linked Gui ID> <display name>`

In our example, this would look something like:
- `/tshop link create subline Subline Tickets`

This should give us a link item which looks something like this:

![image](https://github.com/DNAmaster10/TraincartsTicketShop/assets/44494235/92f18aec-fd6f-446b-8475-ee71d26646e3)

With this new link, we can run:
- `/tshop gui edit mainline`
  
and drag and drop the link item anywhere within the `mainline` gui, even amongst other tickets.

![image(1)](https://github.com/DNAmaster10/TraincartsTicketShop/assets/44494235/ad39b4f3-5caa-436d-a124-69a82d72a48a)

Now, when a player opens the `mainline` gui, they will be able to click the link item. This will redirect them to the `subline` gui.

![Untitled-2024-01-25-2133](https://github.com/DNAmaster10/TraincartsTicketShop/assets/44494235/bea04e28-cb23-4448-9102-7a74238354cd)

It's also possible to set the destination page that a link item will link to.

For example:
- `/tshop link setDestinationPage 10`
  
will make it so that when the link is clicked, it will try to redirect the player to page 10 of the destination gui.






