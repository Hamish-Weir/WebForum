package forum.config.data;

import forum.dao.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import org.springframework.security.crypto.factory.PasswordEncoderFactories;
// import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import forum.dao.services.CommentService;
import forum.dao.services.ForumService;
import forum.dao.services.LikeService;
import forum.dao.services.ThreadService;

@Configuration
@Profile("default")
public class InitialDataLoader {

    private final static Logger log = LoggerFactory.getLogger(InitialDataLoader.class);

	private final static String[][] FORUM_TEMPLATES = {
		// Forums
		// {Title, Description}
        {"Caving", "Forum for discussing Caving"}, 					// FID = 1
        {"Kayaking", "Forum for discussing Kayaking"}, 				// FID = 2
        {"Judo", "Forum for discussing Judo"},						// FID = 3
        {"Computers", "Forum for discussing Computers"},			// FID = 4
        {"Misc", "Forum for discussing Miscellaneous subjects"},	// FID = 5
		{"Empty", "Currently Empty Forum"},
    };

	private final static String[][] USER_TEMPLATES = { 
		// Users
		// {Name, Email, Password}
        {"Hamish", 	"hamish.email@email.com", 	"Password1"},  		// UID = 1
        {"Adrian", 	"adrian.email@email.com", 	"Password2"},  		// UID = 2
		{"Tom", 	"tom.email@email.com",		"Password3"},		// UID = 3
        {"Hannah", 	"hannah.email@email.com", 	"Password4"},		// UID = 4
		{"Ian", 	"ian.email@email.com", 		"Password5"},		// UID = 5
		{"Cait", 	"cait.email@email.com", 	"Password6"},		// UID = 4
		{"Lilly", 	"lilly.email@email.com", 	"Password7"},		// UID = 4
		{"fred", 	"fred.email@email.com", 	"Password8"},		// UID = 4
    };
		
	private final static String[][] THREAD_TEMPLATES = {
		// Threads
		// {FID, UID, Title, Body}
		// TID = 1...
		{"1", "1", "Best caves to start in for beginners?", "I'm new to caving and looking for some beginner-friendly caves in the area. Any recommendations?"},
		{"1", "2", "Essential gear for your first cave trip", "Just wanted to share a list of must-have gear for newcomers: helmet, headlamp (plus spare batteries), sturdy boots, gloves, and a basic first aid kit."},
		{"1", "3", "Spelunking photography tips", "Does anyone have tips for taking good photos underground? The lighting conditions are really challenging and my pictures always come out blurry."},
		{"1", "4", "Mud Cave experience - last weekend", "Went to Mud Cave for the first time this weekend. It was absolutely incredible but SO muddy. My gear is still not clean. Highly recommend it though!"},
		{"1", "5", "Is caving safe? Worried about the risks", "I've always been curious about caving but I'm a bit afraid. How safe is it really? What kind of accidents happen?"},
		{"1", "1", "Crystal cavern visit - photos inside!", "Visited the Crystal Cavern yesterday and the formations were breathtaking. I'll try to upload some photos later. The echo in the main chamber is unreal."},
		{"1", "6", "Caving groups looking for members", "I'm putting together a small caving group for this summer and looking for 2-3 more people. We'll go once a week, mostly beginner to intermediate level."},
		{"1", "3", "Underground rivers - swimming or wading?", "Had the most amazing experience swimming in an underground river last month. Has anyone done that? What precautions do you take?"},

		{"2", "2", "First kayak purchase advice", "I'm looking to buy my first kayak. Budget is around $800. Should I go with a sit-inside or sit-on-top? I plan to use it mostly on lakes."},
		{"2", "4", "Paddle technique improvement", "Struggling with my forward stroke efficiency. My shoulders get sore after just an hour. Any tips on improving technique?"},
		{"2", "5", "Whitewater kayaking - where to start?", "I've done some calm water kayaking and want to progress to whitewater. What级别 of rapids should I start with and where are good training spots?"},
		{"2", "1", "Best paddling spots in the region?", "Looking for recommendations for scenic paddling routes within 2 hours of the city. Preferably with some variety in the water conditions."},
		{"2", "3", "Kayak storage solutions for small apartments", "Living in a small apartment and have no idea how to store a kayak. Anyone have clever solutions? I don't want to hang it from the ceiling."},
		{"2", "6", "Night paddling experience", "Went night kayaking for the first time last weekend with a waterproof headlamp. It was a completely different experience - so many bioluminescent organisms!"},
		{"2", "2", "Decking out - what accessories do you use?", "What are your must-have kayak accessories? I have the basics but wondering if things like dry bags, paddle floats, or splash guards are worth it."},
		{"2", "4", "Kayak fishing - is it worth it?", "Thinking about getting into kayak fishing. Seems like it could be amazing but also complicated. Any experienced kayak fishermen here?"},

		{"3", "1", "Judo vs BJJ - which to start with?", "I'm 30 and want to start martial arts. Torn between Judo and BJJ. Both interest me but I can only commit to one. What are your thoughts?"},
		{"3", "2", "Grip strength training for Judo", "My grips keep failing during randori. What specific exercises do you do to build grip strength for Judo? I know general grip trainers help but wondering about sport-specific drills."},
		{"3", "3", "First competition - nerves and prep", "Signed up for my first local Judo tournament next month. Anyone have tips on managing competition nerves? I'm terrified of embarrassing myself."},
		{"3", "4", "Understanding the belt system", "Can someone explain the actual requirements for each belt in Judo? I feel like my sensei just says 'train more' and I'd like to know what I should be focusing on."},
		{"3", "5", "Injury prevention exercises", "What kind of warm-up and conditioning exercises do you do to prevent injuries? I've had shoulder issues in the past and want to be more proactive."},
		{"3", "1", "Kumikata (grappling) drills that actually work", "Sharing some kumikata drills that helped me improve my grappling game tremendously. The key is repetitive practice with a cooperative partner."},
		{"3", "6", "Judo for self-defense - does it work?", "A friend asked me if Judo is effective for real self-defense situations. I said yes but they were skeptical. What would you tell someone who doubts martial arts for street situations?"},
		{"3", "3", "Throwing techniques - what's your favorite?", "I'm trying to expand my repertoire of throws. What's your go-to throw and why do you like it? I'm currently working on O Goshi and Seoi Nage."},

		{"4", "1", "Best programming language to learn in 2024?", "I'm a complete beginner and want to learn programming. There are so many languages out there. Which one would you recommend for a beginner who wants to eventually work in web development?"},
		{"4", "2", "Understanding REST API design", "I'm building my first REST API and feeling overwhelmed by the best practices. What are the key principles I should follow for good API design?"},
		{"4", "3", "Git merge conflicts - how to handle them", "Just had my first major merge conflict in a team project and it was a nightmare. Any tips for avoiding or resolving merge conflicts more smoothly?"},
		{"4", "4", "Database choice for a new project", "Starting a new project and trying to decide between SQL and NoSQL. The data is mostly structured but might have some unstructured elements. What would you go with?"},
		{"4", "5", "Code review best practices", "Our team just started doing code reviews and they're not going well. People are either too harsh or too lenient. What are some best practices for constructive code reviews?"},
		{"4", "1", "Machine learning for beginners - where to start?", "I have a basic understanding of Python and want to get into machine learning. What's the best roadmap? Should I start with theory or jump into frameworks?"},
		{"4", "2", "Docker vs Kubernetes - when to use what", "I understand Docker containers but I'm confused about when to actually use Kubernetes. Is it overkill for a small team project?"},
		{"4", "6", "How to stay updated with tech without burnout", "There's so much new technology and frameworks coming out all the time. I feel like I'm always behind. How do you stay current without burning out?"},
		{"4", "3", "Debugging strategies that save time", "What are your most effective debugging strategies? I tend to just stare at the code for a long time before asking for help. Looking for better approaches."},

		{"5", "1", "Book recommendations for 2024", "What are the best books you've read this year? Looking for something new to dive into. I've already read ['Project Hail Mary', 'The Midnight Library']."},
		{"5", "2", "Best cooking recipes for busy weeknights", "I work long hours and struggle to cook healthy meals. What are your go-to quick recipes that don't require a trip to the grocery store every night?"},
		{"5", "3", "Travel destinations for solo travelers", "Planning a solo trip for the first time and feeling a mix of excited and nervous. Where's a great destination for a first-time solo traveler? Budget is flexible."},
		{"5", "4", "Home workout routines without equipment", "My gym membership expired and I can't afford a new one right now. What are some effective bodyweight workout routines I can do at home?"},
		{"5", "5", "Music recommendations - discovering new artists", "I feel like my music taste has become stale. How do you discover new artists? What are some great new musicians you've found recently?"},
		{"5", "1", "Gardening for apartment dwellers", "I live in a small apartment with a tiny balcony but I really want to start gardening. What plants are easy to grow in containers?"},
		{"5", "2", "Budget travel hacks that actually work", "I want to travel more but money is tight. What are some legitimate budget travel hacks? Not sketchy stuff, just smart planning strategies."},
		{"5", "6", "Best podcasts for commutes", "My commute is 1.5 hours each way and I want to fill it with something interesting. What podcasts do you recommend? I like a mix of true crime, comedy, and educational content."},
		{"5", "3", "Learning to play a musical instrument as an adult", "I've always wanted to learn guitar but I'm 35 and worried it's too late. Any advice for adult beginners? Is there a best age to start?"},
		{"5", "1", "Currently empty Thread", "Empty Thread"}
	};

	private final static String[][] COMMENT_TEMPLATES = {
		// Comments
		// {TID, UID, Body}
		// CID = 1...

		// TID 1 - Best caves to start in for beginners?
		{"1", "2", "I'd recommend starting with show caves that have guided tours. They're safe, well-marked, and you'll learn the basics without any risk. Once you're comfortable, you can look into introductory caving courses."},
		{"1", "3", "Yeah, show caves are the way to go. I started at Luray Caverns and it was amazing. After a few visits, I joined a local caving club and they took me on my first wild cave."},
		{"1", "7", "What area are you in? I can give more specific recommendations if I know your location."},

		// TID 2 - Essential gear for your first cave trip
		{"2", "1", "Great list! I'd also add a spare helmet light and a whistle. You never know when you might need to signal for help underground."},
		{"2", "4", "Don't forget knee pads! Crawling through tight passages will destroy your knees if you're not protected."},
		{"2", "5", "I second the knee pads. Also, bring more water than you think you need. Dehydration is a real issue in caves."},

		// TID 3 - Spelunking photography tips
		{"3", "2", "Use a tripod if you can carry one. Long exposures (5-15 seconds) with a high ISO setting will give you much better results. A wide-angle lens is also essential."},
		{"3", "6", "I use a GoPro on a stick for most of my cave shots. It's not as high quality but it's much easier to handle in tight spaces. For wider shots, I bring a small DSLR."},
		{"3", "1", "The key is embracing the darkness. Don't try to light everything - let the natural formations emerge from the shadows. It creates more dramatic photos."},

		// TID 4 - Mud Cave experience
		{"4", "5", "Haha, we all know that feeling! Mud Cave is gorgeous but it lives up to the name. Did you get to see the underground waterfall?"},
		{"4", "3", "I went last month too! The mud is actually really fun to slide on. Pro tip: bring a change of clothes for AFTER the cave, not just before."},

		// TID 5 - Is caving safe?
		{"5", "1", "Caving has risks but they're manageable with proper training and gear. The key is never going alone, checking weather forecasts (flash floods are the biggest danger), and going with experienced cavers."},
		{"5", "6", "Statistically, caving is about as dangerous as hiking. The vast majority of accidents happen when people ignore safety protocols. If you train properly and go with a group, it's very safe."},
		{"5", "4", "I used to be terrified of heights and caving actually helped me overcome that fear. There's something really grounding about being underground. Just start slow and trust the process."},

		// TID 6 - Crystal cavern visit
		{"6", "2", "Those photos are incredible! The way the light catches the crystals is mesmerizing. Did you visit during the guided tour or was it a special after-hours visit?"},
		{"6", "5", "I've been wanting to go there for ages. Is it worth the entrance fee? A friend told me it's one of the most beautiful caverns in the state."},

		// TID 7 - Caving groups looking for members
		{"7", "3", "I'm interested! I've been caving for about 2 years now at an intermediate level. What's your experience level and where do you usually go?"},
		{"7", "4", "Count me in! I'm a beginner but I'm eager to learn. Do you provide any training or do you expect members to already know the basics?"},

		// TID 8 - Underground rivers
		{"8", "1", "Swimming in underground rivers is one of the most surreal experiences I've ever had. The water is usually around 55°F (13°C) so a wetsuit is essential. Make sure you know the water depth and currents before jumping in."},
		{"8", "6", "I did this last summer and it was life-changing. We used snorkeling masks which made it even more magical - being able to see everything clearly while floating in total darkness except for your headlamp."},

		// TID 9 - First kayak purchase advice
		{"9", "3", "For lakes as a beginner, I'd definitely go with a sit-on-top. They're more stable, self-draining, and if you fall off (which you will), it's easier to get back on. Check out the Perception Scooter or Native Waterman Zonka."},
		{"9", "5", "Second the sit-on-top recommendation. Also, try to visit a local paddle shop and test drive a few before buying. Fit is super important and you won't know how a kayak feels until you're in it."},
		{"9", "2", "My first kayak was a cheap Walmart special and it was terrible. Spend the extra money on quality - you'll use it more and it'll be more fun. A good used kayak is better than a bad new one."},

		// TID 10 - Paddle technique improvement
		{"10", "1", "Your stroke is probably too far forward. Try to reach with your paddle at about shoulder width, not wider. Also, make sure you're rotating your torso, not just using your arms. That's the #1 mistake beginners make."},
		{"10", "4", "I had the exact same problem. Watching YouTube videos on kayak stroke technique helped a ton. Search for 'Kayak Stroke Technique' by paddling coaches - the visual guides are really helpful."},
		{"10", "6", "Try doing dry-land paddle drills. Hold a broomstick like a paddle and practice the rotation motion in front of a mirror. It sounds silly but it really helps build muscle memory."},

		// TID 11 - Whitewater kayaking
		{"11", "2", "Start with Class II rapids. You'll want to take a whitewater kayaking course first - don't just jump in alone. Look for clubs that offer introductory whitewater programs. They'll teach you wet exits, self-rescue, and reading water."},
		{"11", "5", "I started at Class II and progressed to Class III over about 6 months with regular training. The key is finding a good mentor or instructor. Don't rush it - whitewater has a steep learning curve."},

		// TID 12 - Best paddling spots
		{"12", "3", "The Riverwalk section is beautiful this time of year. Try launching at Miller's Bend and paddling downstream - it's about 8 miles and takes you through some really scenic areas. Sunset is the best time."},
		{"12", "1", "If you don't mind driving 2 hours, the crystal-clear spring river is incredible. It's not as exciting as whitewater but the visibility and wildlife make it worth it. We saw turtles, herons, and even a bear on the bank."},

		// TID 13 - Kayak storage solutions
		{"13", "2", "I use ceiling-mounted pulley system. It costs about $30 and takes up zero floor space. You just lift the kayak up and lock it in place. My apartment feels completely normal otherwise."},
		{"13", "6", "I built a simple A-frame rack in my closet using 2x4s. It's not pretty but it works perfectly and holds my kayak securely. I can still fit my bike in there too."},
		{"13", "4", "What about a kayak hull carrier that goes on top of a car? Even if you don't drive often, you can store it in the garage and mount it when you need it."},

		// TID 14 - Night paddling
		{"14", "1", "Night paddling is magical! The reflections on the water create this otherworldly feeling. Just make sure you have a bright enough light and tell someone your route before you go."},
		{"14", "5", "We did night paddling with glow sticks attached to our paddles. It looked absolutely incredible from the shore. The water was so calm it was like paddling through stars."},

		// TID 15 - Decking out accessories
		{"15", "3", "Dry bags are non-negotiable. I have three different sizes and use them for everything. Paddle float is great for self-rescue practice. Splash guard (or spray skirt if you have a sit-inside) is essential if you're in choppy water."},
		{"15", "1", "I wouldn't skip a bilge pump if you're getting a sit-inside kayak. Also, a paddle leash saved me once when a wave knocked my paddle away. Small things that make a big difference."},

		// TID 16 - Kayak fishing
		{"16", "4", "Kayak fishing is amazing once you get the hang of it. Start with a stable sit-on-top kayak and a simple rod holder. The Native Utopia 13 is popular but pricey. A used Old Town or Perception will work fine to start."},
		{"16", "2", "The biggest challenge is casting without tipping over. Practice in calm water first and keep your weight centered. Once you get the balance down, it's an incredible way to fish."},

		// TID 17 - Judo vs BJJ
		{"17", "5", "Both are excellent! Judo focuses on throws and takedowns, BJJ on ground fighting. If you like the idea of fast-paced action and being thrown around, go Judo. If you prefer strategy and ground work, BJJ. Visit both and see which feels right."},
		{"17", "3", "I did Judo for 2 years then switched to BJJ. My Judo background actually helped a lot with takedowns in BJJ. If you can't decide, many gyms offer both - try a class of each."},

		// TID 18 - Grip strength for Judo
		{"18", "1", "I do farmer's walks with heavy dumbbells 3x per week. Also, towel pull-ups on the pull-up bar are incredible for grip. And don't forget simple grippers - use them while watching TV or reading."},
		{"18", "4", "Randori grip drills are the most sport-specific. Practice gripping the gi and holding on for 30-second intervals while your partner tries to pull away. It builds both strength and endurance."},
		{"18", "6", "Dead hangs from a pull-up bar for max time. I aim for 2 minutes total (can be broken into sets). My grip strength improved dramatically in just a month."},

		// TID 19 - First competition nerves
		{"19", "2", "Competition nerves are normal! I still get them after 5 years of competing. My trick is to focus on the process, not the outcome. Think about executing your techniques well rather than worrying about winning."},
		{"19", "4", "I did a lot of visualization before my first competition - mentally rehearsing every throw and response. It helped me feel prepared. Also, warm up properly and trust your training."},
		{"19", "7", "Pro tip: eat a light carb-heavy meal 2-3 hours before your matches. Low blood sugar makes anxiety so much worse. I learned this the hard way at my second tournament."},

		// TID 20 - Belt system explanation
		{"20", "5", "The belt system varies by federation but generally: white (basics), yellow (fundamental techniques), orange (adding complexity), green (refining), blue (intermediate), brown (advanced), black (expert). Each belt has specific technique requirements your sensei will tell you about."},
		{"20", "1", "Don't focus too much on belts - focus on actually learning the techniques. I promoted faster when I stopped counting belts and started paying attention to what my sensei was trying to teach me."},

		// TID 21 - Injury prevention
		{"21", "3", "Dynamic stretching before practice and static stretching after is essential. I also do rotator cuff exercises with resistance bands - it's saved my shoulders more than once. Don't skip the cool-down!"},
		{"21", "6", "Ukemi (breakfall training) is your best injury prevention. If you know how to fall properly, you'll avoid so many injuries. Make sure you're drilling breakfalls regularly, not just learning new throws."},
		{"21", "2", "I do yoga twice a week for flexibility and core strength. It's made a huge difference in my Judo performance and reduced my soreness significantly."},

		// TID 22 - Kumikata drills
		{"22", "5", "Thanks for sharing! The collar grip battles you mentioned are exactly what I've been struggling with. How long did it take before you started seeing results with those drills?"},
		{"22", "1", "About 3 weeks of consistent practice. The key is to make them part of your regular warm-up, not an extra thing you do. Once they become habit, you'll notice the difference in randori immediately."},

		// TID 23 - Judo for self-defense
		{"23", "4", "Judo is incredibly effective for self-defense because it teaches you to control distance, use an opponent's force against them, and take them to the ground quickly. The throw alone can end a confrontation."},
		{"23", "6", "I've used Judo in real situations twice. The first time, someone grabbed my arm - I used a simple Taiotoshi and he went down hard. The second time, the threat of being thrown was enough."},
		{"23", "1", "The best self-defense is avoiding the situation entirely. But if you can't escape, Judo gives you the physical tools to protect yourself. That said, always prioritize de-escalation."},

		// TID 24 - Favorite throwing techniques
		{"24", "2", "O Soto Gari is my favorite. It's powerful, reliable, and works in almost any situation. Once you master the timing, it's incredibly satisfying to execute."},
		{"24", "5", "I love De Ashi Harai - it's so quick and deceptive. Your opponent doesn't see it coming because it's a light foot sweep rather than a big momentum-based throw."},
		{"24", "3", "Uchi Mata is beautiful when done right. It's one of the most spectacular throws in Judo. I've been working on it for months and finally got a clean one last week. Felt amazing!"},

		// TID 25 - Best programming language
		{"25", "2", "Python is great for beginners because of its simple syntax and huge community. But for web development specifically, JavaScript is essential. I'd suggest starting with Python to learn programming concepts, then moving to JavaScript."},
		{"25", "4", "I'd say start with JavaScript directly if web dev is your goal. You can see results immediately in the browser which is motivating. Plus, Node.js lets you use JavaScript on the backend too."},
		{"25", "6", "HTML and CSS first - don't underestimate how important it is to understand the web before programming for it. Then JavaScript. This path gave me the strongest foundation."},

		// TID 26 - REST API design
		{"26", "1", "Follow these principles: use nouns for resources (GET /users, not GET /getUsers), use proper HTTP methods (GET, POST, PUT, DELETE), version your API, use consistent error codes, and document everything with OpenAPI/Swagger."},
		{"26", "3", "I'd add: keep it simple. Don't over-engineer. Start with basic CRUD endpoints and add complexity only when you need it. HATEOAS is nice in theory but most teams don't actually use it."},
		{"26", "5", "Authentication and authorization should be designed from day one. Don't add them as an afterthought. JWT or OAuth2 are the most common approaches for modern APIs."},

		// TID 27 - Git merge conflicts
		{"27", "2", "Pull frequently! The biggest cause of conflicts is working on outdated code. Also, break your work into smaller, more focused commits. It's easier to resolve conflicts in small changes."},
		{"27", "4", "Use a good IDE with merge conflict resolution tools. VS Code's built-in conflict resolution is really helpful - it shows you both versions side by side and lets you pick sections easily."},
		{"27", "6", "Feature branches are your friend. Keep your main branch clean and work on feature branches. Merge back only when your feature is complete and tested. This minimizes conflicts dramatically."},

		// TID 28 - Database choice
		{"28", "1", "If your data is mostly structured with clear relationships, go with SQL (PostgreSQL is excellent). NoSQL shines when you have flexible schemas or need to scale horizontally. For most projects, PostgreSQL is the safest bet."},
		{"28", "3", "I'd recommend PostgreSQL with JSONB columns. You get the structure of SQL but can store unstructured data in JSONB columns when needed. Best of both worlds."},
		{"28", "5", "Think about your queries, not just your data structure. If you need complex joins and transactions, SQL. If you're storing documents or logs with simple lookups, NoSQL might be better."},

		// TID 29 - Code review best practices
		{"29", "1", "Start with positive feedback - acknowledge good work before suggesting changes. Use 'I' statements ('I think this could be clearer') rather than 'you' statements ('You did this wrong'). Focus on the code, not the person."},
		{"29", "4", "Set clear expectations for what a code review should cover: functionality, readability, performance, and security. This gives reviewers a framework and prevents nitpicking or missing important issues."},
		{"29", "6", "Time-box your reviews. If a review is taking more than 30 minutes, something is wrong. Either the PR is too large or you're overthinking it. Small, focused PRs get better reviews."},

		// TID 30 - Machine learning for beginners
		{"30", "2", "Start with the theory basics - understand what supervised vs unsupervised learning is, what overfitting means, etc. Then move to scikit-learn for traditional ML. Once comfortable, dive into TensorFlow or PyTorch for deep learning."},
		{"30", "5", "I disagree - start with a practical framework and learn theory as you need it. Building projects keeps you motivated. Start with a simple neural network using TensorFlow, then go back and understand the math."},
		{"30", "1", "Andrew Ng's Machine Learning course on Coursera is the gold standard for beginners. It balances theory and practice perfectly. Do the assignments - they're where the real learning happens."},

		// TID 31 - Docker vs Kubernetes
		{"31", "3", "Docker for containerization, Kubernetes for orchestration. If you have 2-5 services running, Docker Compose is probably enough. Kubernetes becomes worth it when you need auto-scaling, self-healing, or complex deployment strategies across multiple machines."},
		{"31", "6", "Kubernetes is absolutely overkill for small projects unless you're specifically learning it for your career. Docker Compose will get you 95% of the way there with a fraction of the complexity."},
		{"31", "2", "I'd add: if you're deploying to a managed service like AWS ECS or Google Cloud Run, you don't need Kubernetes at all. They handle the orchestration for you."},

		// TID 32 - Staying updated with tech
		{"32", "4", "I follow a curated newsletter (like Bytes or TechRadar) and spend 30 minutes a day reading. I don't try to learn everything - I pick 1-2 technologies that are relevant to my work and focus on those."},
		{"32", "1", "The key is accepting that you can't know everything. Even senior engineers don't keep up with every framework. Focus on fundamentals (algorithms, design patterns, system design) - those don't change."},
		{"32", "5", "I subscribe to a few quality YouTube channels and listen to tech podcasts during my commute. It's passive learning that keeps me current without requiring dedicated study time."},
		{"32", "7", "Unsubscribe from tech Twitter. It's a noise machine designed to make you feel FOMO. Follow a few key people or newsletters instead and ignore the rest."},

		// TID 33 - Debugging strategies
		{"33", "2", "Print statements are underrated. I use colored log levels (red for errors, yellow for warnings) and include context in every log message. It sounds basic but it catches 80% of bugs."},
		{"33", "6", "Rubber duck debugging is real. Explain your code line by line to an inanimate object (or a patient colleague). You'll often spot the bug just by articulating what the code is supposed to do."},
		{"33", "3", "Use the debugger properly. Set breakpoints, step through code, and inspect variables. Most developers underuse debuggers and overuse print statements. Learn to use breakpoints effectively."},

		// TID 34 - Book recommendations
		{"34", "2", "I just finished 'The Ministry for the Future' by Kim Stanley Robinson and it was incredible. Also 'Piranesi' by Susanna Clarke if you want something more surreal and beautiful."},
		{"34", "4", "If you liked 'Project Hail Mary', try 'A Memory Called Empire' by Arkady Martine. It's hard sci-fi with great characters and a compelling mystery."},
		{"34", "6", "'The Invisible Life of Addie LaRue' by V.E. Schwab was one of the best books I read last year. It's more literary fiction but absolutely stunning."},

		// TID 35 - Quick weeknight recipes
		{"35", "1", "Sheet pan dinners are my go-to. Chop veggies and protein, toss with oil and spices, roast at 400°F for 25 minutes. Minimal prep, minimal cleanup. Chicken and broccoli is a classic."},
		{"35", "3", "Stir-fry in general is perfect for busy nights. Have frozen veggies and pre-cooked rice on hand. Anything from 15-20 minutes start to finish. The sauce is just soy sauce, garlic, ginger, and a bit of honey."},
		{"35", "5", "Instant Pot is a game changer for weeknight cooking. Throw everything in, set the timer, and you have a complete meal in 30 minutes with zero active work."},

		// TID 36 - Solo travel destinations
		{"36", "1", "Japan is fantastic for solo travelers. It's incredibly safe, easy to navigate, and solo dining is completely normal. The bullet train system makes it easy to move between cities."},
		{"36", "3", "Portugal is underrated for solo travel. It's affordable, English is widely spoken, and the people are warm and welcoming. Lisbon and Porto are especially great for first-time solo travelers."},
		{"36", "5", "Thailand is a classic for a reason. The backpacker trail is well-established, hostels are cheap and social, and you'll never feel lonely. Just be smart about your belongings."},

		// TID 37 - Home workout routines
		{"37", "2", "Push-ups, squats, lunges, planks, and burpees. That's it. You can build a serious workout routine with just these five exercises. I do 4 rounds of each for 45 seconds work, 15 seconds rest."},
		{"37", "4", "Invest in a set of resistance bands. They're cheap, portable, and incredibly versatile. You can target every muscle group with just bands and your bodyweight."},
		{"37", "6", "Look up 'Calisthenics' routines on YouTube. Channels like 'FitnessFAQs' and 'Thenx' have complete programs that require zero equipment. I've been doing them for a year and I'm in the best shape of my life."},

		// TID 38 - Music recommendations
		{"38", "1", "Check out 'Radiohead' if you haven't - their later albums are incredible. Also 'Tame Impala' for something more psychedelic and modern. And 'Khruangbin' for chill instrumental vibes."},
		{"38", "4", "I've been listening to a lot of 'Japanese City Pop' lately - it's this retro Japanese pop genre from the 70s/80s that's having a huge revival. It's incredibly catchy and mellow."},
		{"38", "6", "Spotify's 'Discover Weekly' playlist is actually really good at finding new artists you'll like. I've found so many great musicians through it. Also try 'Radio' feature on individual songs."},

		// TID 39 - Apartment gardening
		{"39", "2", "Start with herbs - basil, mint, and chives are incredibly easy and useful in cooking. They grow well in small pots on a sunny windowsill or balcony."},
		{"39", "4", "Snake plants and pothos are nearly indestructible houseplants. If you kill everything else, you can't kill these. They also improve air quality which is a nice bonus."},
		{"39", "6", "Grow lights changed everything for me. Even with a small balcony, a simple LED grow light lets you grow tomatoes, peppers, and leafy greens year-round."},

		// TID 40 - Budget travel hacks
		{"40", "2", "Flight comparison tools like Skyscanner and Google Flights are essential. Be flexible with dates - flying mid-week is often 30-50% cheaper. Also, consider nearby airports."},
		{"40", "4", "Stay in hostels or use Couchsurfing. It's not just about saving money - you get local insights and often make travel buddies. I've had the best experiences through both."},
		{"40", "6", "Cook your own meals sometimes. Eating out for every meal adds up fast. Airbnbs with kitchens let you make breakfast and some dinners, which saves a lot."},

		// TID 41 - Podcast recommendations
		{"41", "1", "For true crime, try 'Serial' or 'My Favorite Murder'. For comedy, 'Conan O'Brien Needs a Friend' is consistently hilarious. And for educational content, 'Stuff You Should Know' covers an amazing range of topics."}, 
		{"41", "3", "'The Daily' by NYT if you want to stay informed on current events in 20 minutes. Also 'Freakonomics Radio' - it's economics but fascinating and accessible."}, 
		{"41", "5", "'How I Built This' by Guy Raz is incredible if you're into entrepreneurship. Hearing founders tell their origin stories is genuinely inspiring and educational."},
    };

	private final static String[] [] LIKE_TEMPLATES = { 
		// Likes 
		// {CID, UID} 
		// LID = 1... 

		{"1", "3"}, {"1", "4"}, {"1", "7"}, 
		// CID 1 liked by 3 users 
		{"2", "1"}, {"2", "5"}, 
		//CID 2 liked by 2 users 
		{"3", "2"}, 
		// CID 3 liked by 1 user
		{"4", "1"}, {"4", "3"}, {"4", "4"}, 
		// CID 4 liked by 3 users 
		{"5", "2"}, {"5", "6"}, 
		// CID 5 liked by 2 users 
		{"6", "1"}, {"6", "4"}, {"6", "5"}, {"6", "7"}, 
		// CID 6 liked by 4 users 
		{"7", "2"}, {"7", "3"}, 
		// CID 7 liked by 2 users 
		{"8", "1"}, {"8", "2"}, {"8", "6"}, 
		// CID 8 liked by 3 users 
		{"9", "4"}, {"9", "5"}, 
		// CID 9 liked by 2 users 
		{"10", "1"}, {"10", "3"}, {"10", "4"}, 
		// CID 10 liked by 3 users 
		{"11", "2"}, {"11", "6"}, 
		// CID 11 liked by 2 users 
		{"12", "1"}, {"12", "5"}, {"12", "7"}, 
		// CID 12 liked by 3 users 
		{"13", "2"}, {"13", "3"}, 
		// CID 13 liked by 2 users 
		{"14", "4"}, {"14", "5"}, {"14", "6"}, 
		// CID 14 liked by 3 users 
		{"15", "1"}, {"15", "2"}, 
		// CID 15 liked by 2 users 
		{"16", "3"}, {"16", "4"}, {"16", "7"}, 
		// CID 16 liked by 3 users 
		{"17", "5"}, 
		// CID 17 liked by 1 user 
		{"18", "1"}, {"18", "2"}, {"18", "3"}, 
		// CID 18 liked by 3 users 
		{"19", "4"}, {"19", "6"}, 
		// CID 19 liked by 2 users 
		{"20", "2"}, {"20", "5"}, {"20", "7"}, 
		// CID 20 liked by 3 users 
		{"21", "1"}, {"21", "3"}, 
		// CID 21 liked by 2 users 
		{"22", "4"}, {"22", "5"}, {"22", "6"}, 
		// CID 22 liked by 3 users 
		{"23", "1"}, {"23", "2"}, {"23", "3"}, {"23", "4"}, 
		// CID 23 liked by 4 users 
		{"24", "5"}, {"24", "6"}, 
		// CID 24 liked by 2 users 
		{"25", "1"}, {"25", "2"}, {"25", "3"}, 
		// CID 25 liked by 3 users 
		{"26", "4"}, {"26", "5"}, 
		// CID 26 liked by 2 users 
		{"27", "1"}, {"27", "6"}, {"27", "7"}, 
		// CID 27 liked by 3 users 
		{"28", "2"}, {"28", "3"}, 
		// CID 28 liked by 2 users 
		{"29", "1"}, {"29", "4"}, {"29", "5"}, 
		// CID 29 liked by 3 users 
		{"30", "2"}, {"30", "6"}, 
		// CID 30 liked by 2 users 
		{"31", "1"}, {"31", "3"}, {"31", "4"}, 
		// CID 31 liked by 3 users 
		{"32", "5"}, {"32", "7"}, 
		// CID 32 liked by 2 users 
		{"33", "1"}, {"33", "2"}, {"33", "6"}, 
		// CID 33 liked by 3 users 
		{"34", "3"}, {"34", "4"}, 
		// CID 34 liked by 2 users 
		{"35", "1"}, {"35", "5"}, {"35", "6"}, 
		// CID 35 liked by 3 users 
		{"36", "2"}, {"36", "4"}, 
		// CID 36 liked by 2 users 
		{"37", "1"}, {"37", "3"}, {"37", "7"}, 
		// CID 37 liked by 3 users 
		{"38", "5"}, {"38", "6"}, 
		// CID 38 liked by 2 users 
		{"39", "1"}, {"39", "2"}, {"39", "4"}, 
		// CID 39 liked by 3 users 
		{"40", "3"}, {"40", "5"}, 
		// CID 40 liked by 2 users 
		{"41", "1"}, {"41", "4"}, {"41", "6"}, 
		// CID 41 liked by 3 users 
		{"42", "2"}, {"42", "3"}, {"42", "5"}, 
		// CID 42 liked by 3 users 
		};

	// Summary
	// Table 	| Count | Notes
	// Forums	| 5	 	|
	// Users	| 8		|
	// Threads	| 42	|
	// Comments	| 126	| 2-3 comments per thread
	// Likes	| 96	| 0-4 likes per comment

	@Bean
	CommandLineRunner initDatabase(CommentService commentService, ForumService forumService, LikeService likeService, ThreadService threadService, UserService userService) { //PasswordEncoder passwordEncoder
		if (forumService.count() > 0) {
			return args -> {
				log.info("Database already populated. Skipping data initialization.");
			};
		}

		return args -> {

			for (String[] template : USER_TEMPLATES) {
				log.info("Preloading: " + userService.newUser(template[0], template[1], template[2])); //passwordEncoder.encode(template[2])
			}

			for (String[] template : FORUM_TEMPLATES) {
				log.info("Preloading: " + forumService.newForum(template[0],template[1]));
			};

			for (String[] template : THREAD_TEMPLATES) {
				log.info("Preloading: " + threadService.newThread(Long.parseLong(template[0]), Long.parseLong(template[1]), template[2], template[3]));
			}

			for (String[] template : COMMENT_TEMPLATES) {
				log.info("Preloading: " + commentService.newComment(Long.parseLong(template[0]), Long.parseLong(template[1]), template[2]));
			}

			for (String[] template : LIKE_TEMPLATES) {
				log.info("Preloading: " + likeService.newLike(Long.parseLong(template[0]), Long.parseLong(template[1])));
			}
		};
	}
}
