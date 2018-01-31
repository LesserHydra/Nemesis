package com.lesserhydra.nemesis.namers;

import com.lesserhydra.nemesis.wordgen.NameGenerator;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.LivingEntity;

class VindicatorNamer implements MonsterNamer {
	
	private final static NameGenerator nameGenerator = NameGenerator.builder()
			.rule("S", "Name (\\s Nickname)[0.5] \\s Last | Nickname \\s Last")
			.rule("Nickname", "\" the\\s? ((Adjective|Thing) (-|\\s)?)? Thing \"")
			.rule("Adjective", "big | little | golden | ugly | fat | mean | mad | one | two | far | iron | hurt" +
					"| bad | evil | smelly | old | hyper | slow | odd | cruel")
			.rule("Thing", "fist | thumb | knife | axe | hammer | anvil | wolf | boss | butcher | ear | eye | animal" +
					"| bull | shark | saw | fang | apple | freak | llama | moon | giant | egg | bully | bud | nose" +
					"| cash | cat | mug | stake | iron | tiger | nail | meat | green | block | yak | box | rope")
			
			//Name
			.rule("Name", "Short | Italian")
			.rule("Short", "bill y? | bob by? | jack ey? | john ny? | johnson | sam my? | frank ie? | lou (is|ie)?" +
					"| wil (l|iam|son) | tony | mario | mi (ke|c? key) | bugsy | don ny? | butch | mitch | joe y?" +
					"| jake | ben (ny|jamin)? | fred dy? | ted dy? | al bert? | mugsy | rick y? | sal ly? | sonny" +
					"| kevin | ken ny? | davis | dave")
			
			.rule("Italian", "giovannir | antonior | pieror | francescor | iacopor | bartolomeor | niccolor" +
					"| domenicor | lorenzor | andrear | micheler | tommasor | matteor | filippor | bernardor" +
					"| pagolor | simoner | nannir | cristofanor | zanobir | agnolor | leonardor | lucar | bartolor" +
					"| benedettor | stefanor | marcor | lodovicor | guidor | salvestror | niccolaior | masor" +
					"| biagior | martinor | sandror | ceccor | donator | nerir | giorgior | taddeor | meor | carlor" +
					"| frosinor | agostinor | luigir | guasparrer | papir | gherardor | nardor | nofrir | bastianor" +
					"| bertor | pippor | santir | arrigor | guglielmor | mariottor | giustor | marianor | vannir" +
					"| dinor | goror | bettor | ugolinor | battistar | lapor | rinaldor | baldor | rinierir" +
					"| robertor | buonor | ciprianor | gerir | guccior | romolor | baldassarrr | cambior | lazzeror" +
					"| lottor | bernabar | cennir | chimentor | corsor | girolamor | miniator | salvadorer" +
					"| alessandror | amerigor | bindor | duccior | albertor | alessor | ambrogior | bettinor" +
					"| gabbriellor | marchionner | nencior | biancor | bonacorsor | brunor | daniellor | federigor" +
					"| landor | manettor | pacer | riccardo")
			
			//Last
			.rule("Last", "rossi | russo | ferrari | esposito | bianchi | romano | colombo | ricci | marino | greco" +
					"| bruno | gallo | conti | de luca | mancini | costa | giordano | rizzo | lombardi | moretti" +
					"| barbieri | fontana | santoro | mariani | rinaldi | caruso | ferrara | galli | martini | leone" +
					"| longo | gentile | martinelli | vitale | lombardo | serra | coppola | de santis | d'angelo" +
					"| marchetti | parisi | villa | conte | ferraro | ferri | fabbri | bianco | marini | grasso" +
					"| valentini | messina | sala | de angelis | gatti | pellegrini | palumbo | sanna | farina" +
					"| rizzi | monti | cattaneo | morelli | amato | silvestri | mazza | testa | grassi | pellegrino" +
					"| carbone | giuliani | benedetti | barone | rossetti | caputo | montanari | guerra | palmieri" +
					"| bernardi | martino | fiore | de rosa | ferretti | bellini | basile | riva | donati | piras" +
					"| vitali | battaglia | sartori | neri | costantini | milani | pagano | ruggiero | sorrentino" +
					"| d'amico | orlando | negri | mantovani")
			.build();
	
	@Override
	public String generateName(LivingEntity entity) {
		return WordUtils.capitalize(nameGenerator.next(), new char[] {' ', '-', '\"'});
	}
	
}
