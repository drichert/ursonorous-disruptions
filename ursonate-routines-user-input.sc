//////////////////// SEQUENCE DATA AND LOOPING ROUTINE //////////////////////

// run this one second
(
// Run this first to start the input recording routine
var	num_phonemes = 153,
	avg_phoneme_dur = 3,
	input_bufnum = 999, // if this is changed, make sure it's also changed in the var declarations in the next routine
	
	noise_gate_thresh = 0.4,  // adjusts the level threshold for input recording
	
	audio_in_bus_num = 0,
	in_sig_kbus = Bus.control,
	phasor_abus = Bus.audio,
	
	//in_buf = Buffer.alloc(s, 44100 * avg_phoneme_dur * num_phonemes, bufnum: input_bufnum)
	in_buf = Buffer.alloc(s, 44100 * 10, bufnum: input_bufnum)
	;
	
x = Synth(\ur_in, [\abus_in, audio_in_bus_num, \kbus_out, in_sig_kbus, \thresh, noise_gate_thresh]);
y = Synth.after(x, \ur_write_phasor, [\abus_out, phasor_abus, \bufnum, input_bufnum]);
z = Synth.after(y, \ur_in_buf_writer, [\audio_bus_in, audio_in_bus_num, \phasor_bus_in, phasor_abus, \bufnum, input_bufnum]);

Routine({
	loop({
		in_sig_kbus.get({
			arg value;
			// uncomment to display signal level
			// value.postln; 
			if(	value != 0 || value != -0,
				{ y.run(true) },
				{ y.run(false) }
			);
		});
		0.01.wait;
	})
}).play;
)

(
// For testing input writer (Don't run this part unless testing)
// Adjust second arg to input_bufnum above
{ PlayBuf.ar(1, 999, 1.0, loop: 1) }.play
)

// run this third
(
var	num_phonemes = 153,
	avg_phoneme_dur = 3,
	seg_len, // will be set in the routine below
	input_bufnum = 999,
	
	phonemes = Array.newClear(153 + 1),  // Add an extra slot so the indexing starts at 1
	phoneme_dir = "/Users/drichert/Desktop/ursonate/phonemes.mono.new3/",  // full path to phonemes directory
	lspipe = Pipe.new("ls " + phoneme_dir, "r"),
	filename = lspipe.getLine,  // initialize filename to the first filename pulled from `ls` (lspipe)
	matches, // this will be used for regex matches from filenames when populating the phonemes array
	phonemes_ndx, // index for populating phonemes array (array of buffer numbers)
	
	durations = Array.new(27),
 	measures = Array.new(27)
	;


/////// POPULATE phonemes ARRAY //////

while({filename.notNil},{
	matches = filename.findRegexp("([0-9]+)([a-zA-Z]+)");
	phonemes_ndx = matches.at(1).at(1).asInteger;	
	
	if(	phonemes.at(phonemes_ndx).notNil,
		{ phonemes.put(phonemes_ndx, phonemes.at(phonemes_ndx) ++ Buffer.read(s, phoneme_dir ++ filename)); },
		{ phonemes.put(phonemes_ndx, Array.with( Buffer.read(s, phoneme_dir ++ filename))); }
	);
				
	filename = lspipe.getLine;
});
lspipe.close;

/////// END: POUPLATE phonemes ///////



/////// POPULATE durations //////

//0
durations = durations.add(
	Array.with(
		0.5, 0.2, 0.2, 0.25, 0.25, 0.5,
		0.85,
		0.8,
		0.8,
		0.6, 0.7,
		2
	)
);

//1
durations = durations.add(
	Array.with(
		8,
		1.4
	)
);

//2
durations = durations.add(
	Array.with(
		0.5, 0.45, 0.25, 0.25,
		0.6,
		0.4, 0.45, 0.2, 0.15, 0.5, 0.3,
		0.3,		
		0.4, 0.2, 0.1, 0.4, 0.2, 0.3,
		0.3,
		0.2, 0.1, 0.3, 0.1, 0.15, 0.35,
		0.25,
		0.2, 0.35, 0.1, 0.15, 0.3, 0.35,
		0.25,
		0.4, 0.1, 0.1, 0.15, 0.25, 0.5,
		1.55
	)
);

//3
durations = durations.add(
	Array.with(
		0.4, 0.1, 0.1, 0.15, 0.15, 0.45,
		0.35,
		0.7,
		0.45,
		0.75, 1,
		1
	)
);

//4
durations = durations.add(
	Array.with(
		0.45, 0.35, 1.15,
		0.3,
		0.25, 0.5,
		0.25,
		0.25, 0.3, 0.5,
		0.4,
		0.8,   /// PLACEHOLDER FOR MISSING DURATION (fixed)
		2.1, 1.9,
		2.1
	)
);

//5
durations = durations.add(
	Array.with(
		0.7, 0.15, 0.15, 0.15, 0.2, 0.6,
		0.3,
		0.45, 0.25, 0.25, 1.05
	)
);

//6
durations = durations.add(
	Array.with(
		0.55, 0.25, 0.45,
		0.85
	)
);

//7
durations = durations.add(
	Array.with(
		0.75, 0.5, 0.75,
		1.25
	)
);

//8
durations = durations.add(
	Array.with(
		1, 0.3, 0.35, 1,
		0.3, 0.2, 0.3, 1.15
	)
);

//9
durations = durations.add(
	Array.with(
		0.5, 0.25, 0.5, 0.4, 0.6, 0.25, 0.5,
		1.6
	)
);

//10
durations = durations.add(
	Array.with(
		0.35, 0.1, 0.1, 0.15, 0.2, 0.35,
		0.5,
		0.3, 0.25, 0.15, 0.15, 0.15, 0.65,
		1.45
	)
);

//11
durations = durations.add(
	Array.with(
		0.85, 0.95,
		0.6, 0.8,
		0.45, 0.65,
		0.4, 0.55,
		0.35, 0.45,
		0.3, 0.45,
		1,
		0.35
	)
);

//12
durations = durations.add(
	Array.with(
		0.7,
		0.25,
		0.45,
		0.3,
		0.55,
		0.5,
		0.55,
		0.55,
		0.65,
		0.7,
		0.7,
		0.55, 0.15,
		0.6, 0.15,
		0.45, 0.25,
		0.6, 0.25,
		0.5, 0.2,
		0.6, 0.35,
		1.9,
		3,
		1.9
	)
);

//13
durations = durations.add(
	Array.with(
		0.85, 0.9,
		0.5, 0.65,
		0.35, 0.5,
		0.3, 0.45,
		0.3, 0.4,
		0.25, 0.4,
		1.25,
		0.35,
		0.35
	)
);

//14
durations = durations.add(
	Array.with(
		0.45,
		0.35,
		0.45,
		0.4,
		0.55,
		0.55,
		0.75,
		0.5,
		0.65, 
		0.6,
		0.8,
		0.75, 0.15,
		0.65, 0.15,
		0.5, 0.25,
		0.55, 0.25,
		0.4, 0.25,
		0.5, 0.95,
		2,
		2.15,
		1.35
	)
);

//15
durations = durations.add(
	Array.with(
		1.15, 0.9,
		0.45, 0.65,
		0.4, 0.5,
		0.35, 0.4,
		0.3, 0.35,
		0.25, 0.35,
		1.3,
		0.5,
		2.25
	)
);

//16
durations = durations.add(
	Array.with(		
		0.55,
		2.25,
		0.55,
		2.25,
		0.55,
		2.25,
		0.65,
		1.9,
		0.95,
		1.4,
		1.25,
		0.6,
		1.1,
		0.6,
		0.9,
		0.5,
		0.85,
		0.4,
		1,
		0.4,
		1,
		0.4,
		0.7,
		0.25,
		0.7,
		0.25,
		0.65,
		0.2,
		0.65,
		0.2,
		0.6, 
		0.15,
		0.6,
		0.15,
		0.8,
		0.7,
		0.7,
		0.7,
		0.65,
		0.65,
		0.65,
		0.65,
		0.65,
		0.7,
		0.7,
		0.7,
		0.75,
		0.7,
		0.65,
		0.7,
		0.65,
		0.7,
		0.8,
		0.75,
		0.75,
		0.7,
		0.65,
		0.7,
		0.7, 0.3,
		0.65, 0.3,
		0.6, 0.25,
		0.55, 0.3,
		0.55, 0.3,
		0.55, 0.35,
		0.6, 0.3,
		0.6, 0.3,
		0.55, 0.3,
		0.5, 0.3,
		0.5, 0.3,
		0.45, 0.25,
		0.55, 0.25,
		0.5, 0.25,
		0.45, 0.25,
		0.45, 0.25,
		0.5, 0.25,
		0.45, 0.25,
		0.7, 0.25,
		0.6, 0.2,
		0.55, 0.25,
		0.55, 0.25,
		0.55, 0.25,
		0.2,
		0.4, 0.1, 0.1, 0.15, 0.15, 0.35,
		0.3,
		0.95,
		1.75,
		1.9,
		0.65,
		1.5, 
		1.6,
		1.35,
		1.15,	
		0.95,
		3.5
	)
);

//17
durations = durations.add(
	Array.with(
		0.4, 0.35, 0.85,
		0.55,
		0.3, 0.5,
		0.55,
		0.4, 0.45, 0.35,
		0.7,
		0.4, 0.35, 0.7,
		0.3,
		0.3, 0.35, 0.65,
		0.2,
		0.15, 0.25, 0.65,
		0.4, 1.1,
		0.3,
		1.9, 
		0.25,
		2.75,
		0.7,
		0.3,
		0.2,
		0.4,
		0.4,
		0.45,
		0.35, 0.3,
		0.3, 0.35,
		0.35, 0.3, 0.45,
		0.45, 0.25, 0.5, 0.2, 0.2, 0.15, 0.25, 0.4,
		0.4, 0.25, 0.6, 0.15, 0.2, 0.15, 0.2, 0.3, 0.45, 
		0.4, 0.25, 0.65, 0.15, 0.2, 0.2, 0.25, 0.4, 0.6, 2.95, 2.1,
		2.15
	)
);

//18
durations = durations.add(
	Array.with(
		0.4, 0.1, 0.1, 0.1, 0.2, 0.2, 0.25, 0.3, 0.8, 0.35, 0.6,
		0.4,
		0.35, 0.3, 0.7, 0.2, 0.3, 0.15, 0.25, 0.4, 0.4, 0.4, 1.8, 1.6, 
		0.5,
		0.75, 0.15, 0.15, 0.2, 0.25, 0.25, 0.2, 0.2, 0.15, 0.75,
		0.4, 0.25, 0.3,
		0.25
	)
);

//19
durations = durations.add(
	Array.with(
		0.4, 0.2, 0.2, 
		0.45, 0.15, 0.1,
		0.3, 0.15, 0.1,
		0.25, 0.1, 0.1, 0.3,
		0.3, 0.1, 0.1, 0.15,
		0.25, 0.1, 0.1, 0.25,
		0.25, 0.1, 0.1, 0.2, 0.25,
		0.35, 0.1, 0.1, 0.2, 0.3, 
		0.3, 0.1, 0.1, 0.15, 0.25,
		0.25, 0.1, 0.1, 0.15, 0.1, 0.2,
		0.3, 0.05, 0.05, 0.1, 0.05, 0.1,
		0.2, 0.05, 0.05, 0.1, 0.1, 0.15,
		0.25, 0.05, 0.05, 0.1, 0.1, 0.2, 0.35,
		0.25, 0.05, 0.05, 0.05, 0.05, 0.1, 0.3,
		0.2, 0.05, 0.05, 0.05, 0.05, 0.1, 0.4,
		0.2, 0.05, 0.05, 0.2, 0.1, 0.2, 0.25, 0.35,
		0.5, 0.1, 0.1, 0.2, 0.15, 0.15, 0.15, 0.2, 
		0.2, 0.05, 0.05, 0.1, 0.1, 0.2, 0.2, 0.35,
		0.75, 0.1, 0.1, 0.15, 0.05, 0.15, 0.15, 0.25, 0.2, 0.6,
		0.3, 0.05, 0.05, 0.1, 0.1, 0.15, 0.15, 0.15, 0.15, 0.5,
		0.25, 0.5, 0.5, 0.2, 0.1, 0.2, 0.15, 0.15, 0.15, 0.5,
		0.3, 0.2, 0.25, 
		1.25, 
		0.3, 0.2, 0.15, 0.5, 0.15, 0.15, 0.1, 0.65,
		0.45, 0.25, 0.45, 
		0.55, 0.25, 0.35,
		2
	)
);

//20
durations = durations.add(
	Array.with(
		0.5, 0.1, 0.1, 0.15, 0.15, 0.4, 0.55, 0.4, 0.6, 0.35, 0.7,
		0.5,
		0.4, 0.3, 0.65, 0.25, 0.3, 0.4, 0.3, 0.6, 0.6, 0.4, 3.2, 1.5,
		0.5,
		0.7, 0.1, 0.1, 0.25, 0.25, 0.35, 0.2, 0.2, 0.15, 0.7,
		0.35, 0.2, 0.3, 
		0.45, 0.35, 0.6,
		1.3
	)
);

//21
durations = durations.add(
	Array.with(
		0.75,
		0.8,
		0.8,
		0.65,
		0.8,
		0.7,
		0.9, 0.35,
		1.1,
		0.3, 0.25, 0.2, 0.45,
		0.55,
		0.7, 0.3,
		0.75,
		0.3, 0.25, 0.3, 0.25, 0.5,
		0.85,
		0.5, 0.25, 0.65,
		0.55,
		0.25, 0.25, 0.25, 0.25, 0.25, 0.45,
		0.5,
		0.7, 0.4, 0.4, 
		0.55,
		0.25, 0.25, 0.25, 0.25, 0.25, 0.3, 0.4,
		0.2,
		0.6, 0.45, 0.6, 0.2, 0.95,
		0.5,
		0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.2, 0.45,
		0.3,
		0.8, 0.3, 0.55, 0.4, 0.7,
		0.4,
		0.25, 0.25, 0.2, 0.2, 0.25, 0.2, 0.2, 0.2, 0.45,
		0.25,
		0.6, 0.25, 0.4, 0.4, 0.3, 0.7,
		0.15,
		0.25, 0.2, 0.2, 0.25, 0.2, 0.2, 0.25, 0.2, 0.3, 0.45,
		0.2,
		0.5, 0.25, 0.35, 0.2, 0.25, 0.25, 0.65, 
		0.2,
		0.25, 0.25, 0.2, 0.2, 0.2, 0.2, 0.25, 0.2, 0.2, 0.25, 0.5,
		0.55, 0.25, 0.4, 0.25, 0.2, 0.2, 0.5, 0.15, 0.15, 0.2, 0.65,
		0.2,
		0.2, 0.25, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.25, 0.3, 0.25, 0.5, 0.55,
		0.55, 0.25, 0.45, 0.15, 0.2, 0.1, 0.6, 0.1, 0.15, 0.15, 0.75,
		0.55, 0.25, 0.65,
		0.3, 0.25, 0.25, 0.25, 0.2, 0.2, 
		0.1,
		0.3, 0.2, 0.25, 0.25, 0.25, 0.2,
		0.1,
		0.3, 0.2, 0.25, 0.2, 0.15, 2.95,
		0.8,
		0.55, 0.45, 0.4, 0.6, 0.45, 0.2, 0.15, 0.6, 0.1, 0.15, 0.15, 0.85, 
		0.55, 0.3, 0.35,
		0.5,
		0.55, 0.25, 0.3,
		2 
	)
);

//22
durations = durations.add(
	Array.with(
		0.45, 0.1, 0.1, 0.15, 0.2, 0.2, 0.2, 0.35, 0.4, 0.15, 0.6,
		0.35,
		0.4, 0.25, 0.55, 0.2, 0.25, 0.3, 0.3, 0.35, 0.4, 0.35, 1.7, 1.25,
		0.35,
		0.55, 0.1, 0.1, 0.2, 0.15, 0.3, 0.2, 0.15, 0.15, 0.65,
		0.35, 0.2, 0.3, 
		0.5,
		0.35, 0.4, 0.4,
		1.4
	)
);

//23
durations = durations.add(
	Array.with(
		0.65,
		1.15,
		0.2, 0.15, 0.2, 0.45,
		1,
		0.9,
		0.75,
		0.3, 0.35, 0.25, 0.55, 0.45, 0.1,
		1.3,
		0.65,
		0.45,
		0.35, 0.3, 0.3, 0.35, 0.6, 0.45, 0.25,
		1.8,
		0.65,
		0.9,
		0.4, 0.35, 0.4, 0.3, 0.4, 0.6, 0.45, 0.45,
		1.15,
		0.7, 
		0.55,
		0.2, 0.25, 0.25, 0.2, 0.2, 0.25, 0.4, 0.25, 0.3, 0.7, 
		1.4,
		0.8,
		0.35,
		0.25, 0.25, 0.2, 0.25, 0.2, 0.25, 0.25, 0.4, 0.3, 0.3, 0.2, 0.75,
		0.8,
		0.85,
		0.4,
		0.25, 0.25, 0.2, 0.25, 0.2, 0.2, 0.25, 0.25, 0.35, 0.25, 0.5, 0.35, 0.25, 0.2, 0.9,
		1.8,
		0.9,
		1.3,
		0.2, 0.2, 0.25, 0.25, 0.3, 0.25, 0.3, 0.2, 0.35, 0.25, 0.3, 0.35, 0.25, 0.55, 0.3, 0.2, 0.15, 0.5, 0.15, 0.2, 0.15, 1, 
		2,
		1.15,
		1,
		0.3, 0.2, 0.25, 0.3, 0.25, 0.2, 0.2, 0.2, 0.25, 0.25, 0.3, 0.25, 0.25, 0.2, 0.4, 0.35, 0.45, 
		0.3, 
		0.45, 0.25, 0.15, 0.75, 
		0.2,
		0.15, 0.15, 0.2, 1,
		0.5, 0.3, 0.55,
		2,
		2.2,
		1.8,
		0.3, 0.2, 0.25, 0.3, 0.25, 0.2, 
		0.2, 0.2, 0.25, 0.25, 0.3, 0.25, 
		0.25, 0.2, 0.2, 0.2, 0.25, 0.25,
		0.3, 0.25, 0.3, 0.2, 0.35, 0.25,
		2.4, 0.4, 1.2,
		0.7,
		0.75, 0.3, 0.3, 0.9,
		0.4,
		0.5, 0.25, 0.2, 0.95,
		0.3,
		0.45, 0.25, 0.6,
		0.6,
		0.6, 0.25, 0.75,
		2.25
	)
);

//24
durations = durations.add(
	Array.with(
		0.8, 0.75,
		0.45, 0.65,
		0.4, 0.55,
		0.3, 0.45,
		0.3, 0.4, 
		0.25, 0.45,
		0.65, 
		0.2,
		0.2
	)
);

//25
durations = durations.add(
	Array.with(
		0.15,
		0.25,
		0.2,
		0.25,
		0.2,
		0.25,
		0.15,
		0.2,
		0.15,
		0.2,
		0.15,
		0.25,
		0.15, 
		0.25,
		0.2, 
		0.2,
		0.1,
		0.2,
		0.2, 
		0.2,
		0.15,
		0.25,
		0.25,
		0.55,
		0.25,
		0.45,
		0.25,
		0.45,
		0.2,
		0.4,
		0.25,
		0.4,
		0.2,
		0.35,
		0.2,
		0.25,
		0.4,
		0.25,
		0.4,
		0.25,
		0.4,
		0.25,
		0.4,
		0.25,
		0.35,
		0.25,
		0.45,
		0.25,
		0.35,
		0.6,
		0.4,
		0.4,
		0.3,
		0.4, 
		0.3,
		0.45,
		0.3,
		0.4,
		0.3,
		0.4,
		0.3,
		0.45,
		0.45,
		0.4,
		0.45,
		0.4,
		0.4,
		0.4,
		0.45,
		0.4,
		0.4,
		0.4,
		0.55,
		0.2,
		0.65,
		0.8,
		0.55,
		0.8,
		0.15,
		0.55,
		0.6,
		0.5,	
		0.55,
		0.2,
		0.55,
		0.55,
		0.5,
		0.7,
		0.2,
		0.5, 0.2,
		0.45, 0.15,
		0.4, 0.15,
		0.45, 0.2,
		0.45, 0.15,
		0.45, 0.2,
		0.45, 0.15,
		0.4, 0.15,
		0.2,
		0.45, 0.2,
		0.5, 0.15,
		0.55, 0.15,
		0.6, 0.15,
		0.3,
		0.45, 0.3,
		0.6, 0.3,
		0.4, 0.25,
		0.6, 0.3,
		0.4, 0.3,
		0.4, 0.3,
		0.4, 0.25,
		0.4, 0.2,
		0.2,
		0.4, 0.25,
		0.4, 0.25,
		0.2,
		0.4, 0.25,
		0.45, 0.2,
		0.4, 0.2,
		0.4, 0.25,
		0.35, 0.2,
		0.4, 0.25,
		0.35, 0.25,
		0.4, 0.25,
		0.25,
		0.4, 0.2,
		0.4, 0.2,
		0.35, 0.25,
		0.5, 1,
		1.7,
		1.5, 0.3, 0.85,
		0.95, 1.15,
		0.55,
		0.8, 0.85,
		0.65, 0.85,
		0.55,
		0.8, 0.9,
		0.85, 0.9,
		1.4
		)
		++
		Array.with(
		0.4, 0.1, 0.1, 0.15, 0.2, 0.5, 0.3, 0.4, 0.8, 1,
		0.75,
		0.4, 0.2, 0.2, 0.7, 0.15, 0.35, 0.2, 0.3, 0.35, 0.75, 0.45, 0.7, 3.2, 1.3,
		1.6,
		0.65, 0.15, 0.15, 0.15, 0.15, 0.4, 0.15, 0.2, 0.15, 0.75,
		0.4, 0.25, 0.3,
		0.45,
		0.45, 0.35, 0.4,
		0.8,
		0.6, 0.25, 0.25, 0.75, 0.15, 0.2, 0.15, 0.75,
		0.2,
		0.45, 0.3, 0.4,
		0.2,
		0.45, 0.25, 0.45,
		0.95,
		0.3, 0.1, 0.1, 0.1, 0.1, 0.3,
		0.65,
		0.3, 0.2, 0.1, 0.1, 0.1,
		0.1, 0.1, 0.1, 0.1, 
		0.15, 0.1, 0.1, 0.1,
		0.15, 0.1, 0.1, 0.1,
		0.1, 0.05, 0.1, 0.1,
		0.15, 0.1, 0.15, 0.25, 0.45, 0.6,
		1.6
	)
);

//26
durations = durations.add(
	Array.with(
		0.35, 0.75, 0.45, 0.45, 0.1, 0.85, 0.45, 0.4, 0.1, 0.1, 2.5,
		0.45,
		0.4, 0.7, 0.45, 0.1, 0.75, 0.4, 0.05, 0.1, 0.15, 0.2, 1.7,
		0.45, 0.45, 0.75,
		0.95,
		0.6, 0.15, 0.1, 0.15, 0.15, 0.35,
		0.8,
		0.4, 0.95,
		0.3,
		0.45, 1.2,
		0.2,
		0.5, 0.1, 0.1, 8.45,
		10
	)
);

// SECTION 2 //

//27
durations = durations.add(
	Array.with(
		1.7,
		5.95,
		1.4,
		0.95, 0.89, 0.91, 0.86, 0.9,
		2.08,
		4.98,
		1.03,
		0.96, 0.81, 0.8, 0.85, 1.02,
		1.96,
		5.93,
		0.94,
		1.66, 0.56, 0.86, 0.5, 1.01,
		0.77,
		6.35,
		0.68,
		0.95, 0.88, 0.86, 0.92, 0.91,
		1.08,
		7.77,
		2.64
	)
);

//28
durations = durations.add(
	Array.with(
		6.23,
		0.5,
		0.8, 0.8, 0.79, 0.81, 1.15,
		0.8,
		5.71,
		0.63,
		0.9, 0.91, 0.92, 0.83, 1.26, 
		0.79,
		5.63,
		0.46,
		1.61, 0.68, 0.64, 0.58, 1.1,
		0.55,
		5.96,
		0.75,
		0.76, 0.62, 0.66, 0.83, 0.93,
		0.75,
		6.9,
		2.26
	)
);

//29
durations = durations.add(
	Array.with(
		6.18,
		0.52,
		1.01, 0.81, 0.86, 0.88, 1.13,
		1.8,
		7.03,
		0.76,
		0.98, 0.81, 0.85, 0.83, 1.64,
		1.24,
		6.55,
		0.62,
		1.41, 0.66, 1, 0.47, 1.39,
		1.04,
		6.46,
		0.54,
		0.83, 0.97, 0.95, 1.35, 1.04,
		1.32,
		9.13,
		7.15
		
	)
);




/////// POPULATE measures /////

//0
measures = measures.add(
	Array.with(
		1, 2, 3, 4, 5, 6,
		0,
		7,
		0,
		8, 9,
		0
	)
);

//1
measures = measures.add(
	Array.with(
		10,
		0
	)
);

//2
measures = measures.add(
	Array.with(
		11, 12, 13, 2,
		0,
		11, 12, 13, 2, 1, 2,
		0,
		12, 13, 2, 1, 2, 3,
		0,
		13, 2, 1, 2, 3, 4,
		0,
		2, 1, 2, 3, 4, 5,
		0,
		1, 2, 3, 4, 5, 6,
		0
	)
);

//3
measures = measures.add(
	Array.with(
		1, 2, 3, 4, 5, 6,
		0,
		7,
		0,
		8, 9,
		0
	)
);

//4
measures = measures.add(
	Array.with(
		14, 15, 16,
		0,
		17, 9,
		0,
		18, 19, 20,
		21,
		0,
		22, 23,
		0
	)
);

//5
measures = measures.add(
	Array.with(
		24, 13, 13, 25, 26, 27,
		0,
		28, 29, 28, 30
	)
);

//6
measures = measures.add(
	Array.with(
		31, 13, 13,
		0
	)
);

//7
measures = measures.add(
	Array.with(
		32, 19, 20,
		0
	)
);

//8
measures = measures.add(
	Array.with(
		28, 29, 28, 33,
		28, 29, 28, 30
	)
);

//9
measures = measures.add(
	Array.with(
		31, 13, 13, 0, 31, 13, 34,
		0
	)
);

//10
measures = measures.add(
	Array.with(
		1, 2, 3, 4, 5, 6,
		0,
		6, 34, 35, 36, 13, 1,
		0
	)
);

//11
measures = measures.add(
	Array.with(
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		13,
		2
	)
);

//12
measures = measures.add(
	Array.with(
		38,
		39,
		40,
		41,
		42,
		43,
		44,
		45,
		46,
		47,
		48,
		47, 49,
		48, 49,
		47, 50,
		48, 50,
		47, 7,
		48, 7,
		0,
		51,
		0
	)
);

//13
measures = measures.add(
	Array.with(
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		13,
		2
	)
);

//14
measures = measures.add(
	Array.with(
		38,
		39,
		40,
		41,
		42,
		43,
		44,
		45,
		46,
		47,
		48,
		47, 49,
		48, 49,
		47, 50,
		48, 50,
		47, 7,
		48, 7,
		51,
		0
	)
);


//15
measures = measures.add(
	Array.with(
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		13,
		2,
		0
	)
);

//16
measures = measures.add(
	Array.with(		
		2,
		0,
		2,
		0,
		2,
		0,
		2,
		0,		
		2,
		0,
		39,
		0,
		39,
		0,
		39,
		0,
		39,
		0,
		39,
		0,
		39,
		0,
		41,
		0,
		41,
		0,
		41,
		0,
		41,
		0,
		41,
		0,
		41,
		0,
		52,
		52,
		52,
		52,
		52,
		52,
		53,
		53,
		53,
		53,
		53,
		53,
		54,
		54,
		54,
		54,
		54,
		55,
		55,
		55,
		55,
		55,
		47, 56,
		47, 56,
		47, 56,
		47, 56,
		47, 56,
		47, 56,
		47, 50,
		47, 50,
		47, 50,
		47, 50,
		47, 50,
		47, 50,
		47, 57,
		47, 57,
		47, 57,
		47, 57,
		47, 57,
		47, 57,
		48, 57,
		48, 57,
		48, 57,
		48, 57,
		48, 57,
		48, 57,
		0,
		58, 2, 3, 4, 5, 6,
		0,
		57,
		0,
		51,
		0,
		51,
		51,
		51,
		51,
		51,
		0
	)
);

//17
measures = measures.add(
	Array.with(
		14, 15, 16,
		0,
		17, 9,
		0,
		18, 19, 20,
		0,
		14, 15, 16,
		0,
		59, 15, 16,
		0,
		15, 15, 16,
		15, 16,
		0,
		17,
		0,
		9,
		0,
		60,
		0,
		61,
		62,
		63,
		18, 21,
		18, 19,
		18, 19, 20,
		14, 15, 16, 17, 9, 18, 19, 20,
		14, 15, 16, 17, 9, 18, 29, 20, 21,
		14, 15, 16, 17, 9, 18, 19, 20, 21, 22, 23,
		0
	)
);

//18
measures = measures.add(
	Array.with(
		1, 2, 3, 4, 5, 6, 0, 7, 0, 8, 9,
		0,
		14, 15, 16, 17, 9, 18, 19, 20, 21, 0, 22, 23,
		0,
		24, 13, 13, 25, 26, 27, 28, 29, 28, 30,
		31, 13, 13,
		0
	)
);

//19
measures = measures.add(
	Array.with(
		64, 13, 13,
		24, 13, 13,
		31, 13, 13,
		64, 13, 13, 29,
		24, 13, 13, 29,
		31, 13, 13, 29,
		64, 13, 13, 25, 26,
		24, 13, 13, 25, 26,	
		31, 13, 13, 25, 26,
		64, 13, 13, 25, 26, 27,
		24, 13, 13, 25, 26, 27,
		31, 13, 13, 25, 26, 27,
		64, 13, 13, 25, 26, 27, 28,
		24, 13, 13, 25, 26, 27, 28,
		31, 13, 13, 25, 26, 27, 28,
		64, 13, 13, 25, 26, 27, 28, 29,
		24, 13, 13, 25, 26, 27, 28, 29,
		31, 13, 13, 25, 26, 27, 28, 29,
		64, 13, 13, 25, 26, 27, 28, 29, 28, 30,
		24, 13, 13, 25, 26, 27, 28, 29, 28, 30,
		31, 13, 13, 25, 26, 27, 28, 29, 28, 30,
		31, 13, 13,
		65,
		28, 29, 28, 30, 28, 29, 28, 30,
		31, 13, 13,
		31, 13, 34,
		0
	)
);

//20
measures = measures.add(
	Array.with(
		1, 2, 3, 4, 5, 6, 0, 7, 0, 8, 9,
		0,
		14, 15, 16, 17, 9, 18, 19, 20, 21, 0, 22, 23,
		0,
		24, 13, 13, 25, 26, 27, 28, 29, 28, 30,
		31, 13, 13, 
		32, 19, 20,
		0
	)
);

//21
measures = measures.add(
	Array.with(
		66,
		0,
		32,
		0,
		66,
		0,
		32, 67,
		0,
		68, 68, 0, 66,
		0,
		32, 69,
		0,
		68, 68, 68, 68, 66,
		0,
		32, 0, 19,
		0,
		68, 68, 68, 68, 68, 66,
		0,
		32, 19, 20,
		0,
		68, 68, 68, 68, 68, 68, 66,
		0,
		32, 19, 20, 0, 28,
		0,
		68, 68, 68, 68, 68, 68, 68, 66,
		0,
		32, 19, 20, 28, 29,
		0,
		68, 68, 68, 68, 68, 68, 68, 68, 66,
		0,
		32, 19, 20, 28, 29, 28,
		0,
		68, 68, 68, 68, 68, 68, 68, 68, 68, 66,
		0,
		32, 19, 20, 28, 29, 28, 33,
		0,
		68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 66,
		32, 19, 20, 28, 29, 28, 33, 28, 29, 28, 30,
		0,
		68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 66,
		32, 19, 20, 28, 29, 28, 33, 28, 29, 28, 30,
		31, 13, 13,
		68, 68, 68, 68, 68, 68,
		0,
		68, 68, 68, 68, 68, 68,
		0,
		68, 68, 68, 68, 68, 66,
		0,
		32, 19, 20, 0, 28, 29, 28, 33, 28, 29, 28, 30,
		31, 13, 13,
		0,
		31, 13, 34,
		0
	)
);

//22
measures = measures.add(
	Array.with(
		1, 2, 3, 4, 5, 6, 0, 7, 0, 8, 9,
		0,
		14, 15, 16, 17, 9, 18, 19, 20, 21, 0, 22, 23,
		0,
		24, 13, 13, 25, 26, 27, 28, 29, 28, 30,
		31, 13, 13,
		0,
		32, 19, 20,
		0
	)
);

//23
measures = measures.add(
	Array.with(
		66,
		0,
		68, 68, 68, 32,
		0,
		66,
		0,
		68, 68, 68, 32, 0, 67,
		0,
		66,
		0,
		68, 68, 68, 68, 32, 0, 69,
		0,
		66,
		0,
		68, 68, 68, 68, 68, 32, 70, 20,
		0,
		66,
		0,
		68, 68, 68, 68, 68, 68, 32, 70, 20, 28,
		0,
		66,
		0,
		68, 68, 68, 68, 68, 68, 68, 32, 70, 20, 28, 29,
		0,
		66,
		0,
		68, 68, 68, 68, 68, 68, 68, 68, 32, 70, 20, 28, 29, 28, 33,
		0,
		66,
		0,
		68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 68, 32, 70, 20, 28, 29, 28, 		33, 28, 29, 28, 30,
		0,
		66,
		0,
		68, 68, 68, 68, 68, 68, 68, 12, 68, 68, 12, 68, 68, 68, 32, 70, 20,
		0,
		28, 29, 28, 33,
		0,
		28, 29, 28, 30,
		31, 13, 13,
		0,
		71,
		0,
		68, 68, 68, 68, 68, 68,
		68, 68, 68, 68, 68, 68,
		68, 68, 68, 68, 68, 68,
		68, 68, 68, 68, 68, 68,
		72, 73, 74,
		0,
		28, 29, 28, 33,
		0,
		28, 29, 28, 30,
		0,
		31, 13, 13,
		0,
		31, 13, 34,
		0
	)
);

//24
measures = measures.add(
	Array.with(
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		31, 37,
		13,
		2,
		0
	)
);

//25
measures = measures.add(
	Array.with(
		38,
		0,
		2,
		0,
		38,
		0,
		2,
		0,
		38,
		0,
		2,
		0,
		38,
		0,
		2,
		0,
		38,
		0,
		2,
		0,
		38,
		0,
		39,
		40,
		39,
		40,
		39,
		40,
		39,
		40,
		39,
		40,
		39,
		40,
		0,
		41,
		42,
		41,
		42,
		41,
		42,
		41,
		42,
		41,
		42,
		41,
		42,
		0,
		43,
		44,
		43,
		44,
		43,
		44,
		43,
		44,
		43,
		44,
		43,
		44,
		0,
		45,
		46,
		45,
		46,
		45,
		46,
		45,
		46,
		45,
		46,
		45,
		46,
		0,
		47,
		48,
		47,
		48,
		0,
		47,
		48,
		47,
		48,
		0,
		47,
		48,
		47,
		48,
		0,
		47, 49,
		48, 49,
		47, 49,
		48, 49,
		47, 49,
		48, 49,
		47, 49,
		48, 49,
		0,
		47, 49,
		48, 49,
		47, 49,
		48, 49,
		0,
		47, 50,
		48, 50,
		47, 50,
		48, 50,
		47, 50,
		48, 50,
		47, 50,
		48, 50,
		0,
		47, 50,
		48, 50,
		0,
		47, 7,
		48, 7,
		47, 7,
		48, 7,
		47, 7,
		48, 7,
		47, 7,
		48, 7,
		0
		)
		++
		Array.with(
		47, 7,
		48, 7,
		47, 7,
		48, 7,
		0,
		51, 0, 51,
		51, 51,
		0,
		51, 51,
		51, 51,
		0,
		51, 51,
		51, 51,
		0,
		1, 2, 3, 4, 5, 6, 0, 7, 0, 51,
		0,
		14, 15, 0, 16, 17, 9, 18, 19, 20, 0, 21, 0, 22, 23,
		0,
		24, 13, 13, 25, 26, 27, 28, 29, 28, 30,
		31, 13, 13,
		0,
		32, 19, 20,
		0,
		28, 29, 28, 33, 28, 29, 28, 30,
		0,
		31, 13, 13,
		0,
		31, 13, 34,
		0,
		1, 2, 3, 48, 5, 6,
		0,
		6, 34, 35, 36, 13,
		34, 35, 36, 13,
		34, 35, 36, 13,
		34, 35, 36, 13,
		34, 35, 36, 13,
		34, 35, 36, 13, 0, 1,
		0
	)
);


//26
measures = measures.add(
	Array.with(		
		1, 2, 0, 1, 2, 3, 0, 1, 2, 3, 4,
		0,
		1, 2, 1, 2, 3, 1, 2, 3, 4, 5, 6,
		75, 76, 76,
		0,
		24, 13, 13, 25, 26, 27,
		0,
		1, 2,
		0,
		1, 39,
		0,
		1, 2, 3, 4,
		0
	)
);

// SECTION 2 //

//27
measures = measures.add(
	Array.with(
		0,
		10,
		0,
		13, 13, 13, 13, 13,
		0,
		10,
		0,
		34, 34, 34, 34, 34,
		0,
		10,
		0,
		24, 0, 13, 0, 13,
		0,
		10,
		0,
		77, 34, 0, 77, 34,
		0,
		10,
		0
	)
);

//28
measures = measures.add(
	Array.with(
		78,
		0,
		13, 13, 13, 13, 13,
		0,
		78,
		0,
		34, 34, 34, 34, 34, 
		0,
		78,
		0,
		24, 0, 13, 0, 13,
		0,
		78,
		0,
		79, 34, 0, 77, 34,
		0,
		78,
		0
	)
);

//29
measures = measures.add(
	Array.with(
		10,
		0,
		13, 13, 13, 13, 13,
		0,
		10,
		0,
		34, 34, 34, 34, 34,
		0,
		10,
		0,
		24, 0, 13, 0, 13,
		0,
		10,
		0, 
		77, 34, 0, 77, 34,
		0,
		10, 
		0
	)
);





r = Routine({
	loop({
	measures.do({ arg item, i;
		d = durations.at(i);
		item.do({ arg item, i;
			// d.at(i).postln;
			if( item > 0, // If it's not a pause
				{
					// 60/40: ursonate pre-recording more likely
					if( [true, true, true, false, false].choose,
						{ // play from pre-recorded audio
							x = Synth(\buf_player, [\bufnum, phonemes.at(item).choose, \dur, d.at(i)]);
							d.at(i).wait;
						x.free;
						},
						{ // play from input buffer
							Synth(\buf_rand_seg_player, [\bufnum, input_bufnum, \num_segs, num_phonemes, \dur, d.at(i)]);
							d.at(i).wait;
						}
					)	
					
				},
				{
					d.at(i).wait;
				}
			);
		});
	});
	"****e****".postln;
	2.wait;
	})
});

r.play;
)

r.stop;
		