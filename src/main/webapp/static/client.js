$(function($) {
	
	var TIMEOUTID;
	
	var Game = Backbone.Model.extend({});
	var Games = Backbone.Collection.extend({
		model: Game,
		url: '/games'
	});
	var games = new Games();
	
	var GamesList = Backbone.View.extend({
		el: $("#boardlist"),
		render: function() {
			var $el = $(this.el);
			_.forEach(this.model.models, function(m) {
				$el.append("<li><a href='#" + m.get("id") +
						"'>" + m.get('name') + "</a></li>");	
			})
			return this;
		}
	});
	
	var GameBoard = Backbone.View.extend({
		el: $("#board"),
		render: function(id) {
			id = parseInt(id, 10);
			if (this.model.models.length === 0) {
				return this;
			}
			var $el = $(this.el);
			$el.empty();
			var model = this.model.models[id - 1];
			var board = model.get("board");
			
			var i, j;
			for (i = 0; i < board.length; i++) {
				var row = board[i];
				var tr = $("<tr></tr>");
				$el.append(tr);
				
				for (j = 0; j < row.length; j++) {
					var col = row[j],
						td = $("<td></td>"),
						cursor = "default",
						symbol;
					tr.append(td);
					if (col === 1) {
						symbol = "O";
					} else if (col === 2) {
						symbol = "X";
					} else {
						symbol = "";
						cursor = "pointer";
						td.click(function (row, col) {
							// nested function because of closure/for-loop bug
							return function() {
								row[col] = ($("input:checked").val() === "X" ? 2 : 1);
								model.save({}, {
									success: function() {
										gameBoard.render(id);
									}
								});
							};
						}(row, j));
					}
					td.css("cursor", cursor);
					td.append(symbol);
				}
			}
			
			/*
			 * Clear board button
			 */
			$("#clear-board").unbind('click');
			$("#clear-board").click(function (){
				for (i = 0; i < board.length; i++) {
					for (j = 0; j < board[i].length; j++) {
						board[i][j] = 0;
					}
				}
				model.save({}, {
					success: function() {
						gameBoard.render(id);
					}
				});
			})
			
			return this;
		}
	})
	
	var gamesList = new GamesList({model: games});
	var gameBoard = new GameBoard({model: games});
	
	games.bind('reset', gamesList.render);
	
	games.fetch({
		success: function(model, response, options) {
			gamesList.render();
		}
	});
	
	/*
	 * Routing stuff
	 */
	var Workspace = Backbone.Router.extend({
		  routes: {":id": "id"}
	});
	var router = new Workspace();
	router.on('route:id', function(id) {
		clearTimeout(TIMEOUTID);
		gameBoard.render(id);

		// setup ajax polling
		function refresh () {
			games.fetch({
				success: function(model, response, options) {
					clearTimeout(TIMEOUTID);
					gameBoard.render(id);
					TIMEOUTID = setTimeout(refresh, 2000);
				}
			});
		}
		refresh();
	})
	Backbone.history.start();
	router.navigate("1");
});