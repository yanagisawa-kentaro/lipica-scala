package org.lipicalabs.lipica.core.net.lpc.message


import org.junit.runner.RunWith
import org.lipicalabs.lipica.core.crypto.digest.DigestValue
import org.lipicalabs.lipica.core.net.lpc.LpcMessageCode
import org.lipicalabs.lipica.core.utils.ImmutableBytes
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
  *
  * @since 2015/12/12
  * @author YANAGISAWA, Kentaro
  */
@RunWith(classOf[JUnitRunner])
class LpcMessagesTest extends Specification {
	 sequential

	 "test BlockHashesMessage" should {
		 "be right" in {
			val original = BlockHashesMessage(Seq(DigestValue.parseHexString("0101010101010101010101010101010101010101010101010101010101010101")))
			 val encoded = original.toEncodedBytes
			 val decoded: BlockHashesMessage = decodeMessage(LpcMessageCode.BlockHashes.asByte, encoded)

			 decoded.code mustEqual original.code
			 decoded.blockHashes mustEqual original.blockHashes
		 }
	 }

	"test BlocksMessage" should {
		"be right" in {
			val decoded: BlocksMessage = decodeMessage(LpcMessageCode.Blocks.asByte, ImmutableBytes.parseHexString("f901fff901fcf901f7a0fbce9f78142b5d76c2787d89d574136573f62dce21dd7bcf27c7c68ab407ccc3a01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d493479415caa04a9407a2f242b2859005a379655bfb9b11a0689e7e862856d619e32ec5d949711164b447e0df7e55f4570d9fa27f33ca31a2a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b9010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000830201c008832fefd880845504456b80a05b0400eac058e0243754f4149f14e5c84cef1c33a79d83e21c80f590b953fd60881b4ef00c7a4dae1fc0c0"))

			decoded.blocks.size mustEqual 1
			decoded.code mustEqual LpcMessageCode.Blocks.asByte
			val block = decoded.blocks.head
			block.transactions.isEmpty mustEqual true
			block.blockNumber mustEqual 8
			block.hash.toHexString mustEqual "2bff4626b9854e88c72ccc5b47621a0a4e47ef5d97e1fa7c00560f7cd57543c5"
			block.stateRoot.toHexString mustEqual "689e7e862856d619e32ec5d949711164b447e0df7e55f4570d9fa27f33ca31a2"

			val encoded = decoded.toEncodedBytes
			encoded.toHexString mustEqual "f901fff901fcf901f7a0fbce9f78142b5d76c2787d89d574136573f62dce21dd7bcf27c7c68ab407ccc3a01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d493479415caa04a9407a2f242b2859005a379655bfb9b11a0689e7e862856d619e32ec5d949711164b447e0df7e55f4570d9fa27f33ca31a2a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b9010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000830201c008832fefd880845504456b80a05b0400eac058e0243754f4149f14e5c84cef1c33a79d83e21c80f590b953fd60881b4ef00c7a4dae1fc0c0"
		}
	}

	"test GetBlockHashesByNumberMessage" should {
		"be right" in {
			val decoded: GetBlockHashesByNumberMessage = decodeMessage(LpcMessageCode.GetBlockHashesByNumber.asByte, ImmutableBytes.parseHexString("c464822710"))

			decoded.code mustEqual LpcMessageCode.GetBlockHashesByNumber.asByte
			decoded.blockNumber mustEqual 100
			decoded.maxBlocks mustEqual 10000

			decoded.toEncodedBytes.toHexString mustEqual "c464822710"
		}
	}

	"test GetBlockHashesMessage" should {
		"be right" in {
			val decoded: GetBlockHashesMessage = decodeMessage(LpcMessageCode.GetBlockHashes.asByte, ImmutableBytes.parseHexString("e4a05ad1c9caeade4cdf5798e796dc87939231d9c76c20a6a33fea6dab8e9d6dd009820100"))

			decoded.code mustEqual LpcMessageCode.GetBlockHashes.asByte
			decoded.maxBlocks mustEqual 256
			decoded.bestHash.toHexString mustEqual "5ad1c9caeade4cdf5798e796dc87939231d9c76c20a6a33fea6dab8e9d6dd009"

			decoded.toEncodedBytes.toHexString mustEqual "e4a05ad1c9caeade4cdf5798e796dc87939231d9c76c20a6a33fea6dab8e9d6dd009820100"
		}
	}

	"test GetBlocksMessage" should {
		"be right" in {
			val decoded: GetBlocksMessage = decodeMessage(LpcMessageCode.GetBlocks.asByte, ImmutableBytes.parseHexString("f8a5a0497dcbd12fa99ced7b27cda6611f64eb13ab50e20260eec5ee6b7190e7206d54a00959bdfba5e54fcc9370e86b7996fbe32a277bab65c31a0102226f83c4d3e0f2a001a333c156485880776e929e84c26c9778c1e9b4dcb5cd3bff8ad0aeff385df0a0690e13595c9e8e4fa9a621dfed6ad828a6e8e591479af6897c979a83daf73084a0b20f253d2b62609e932c13f3bca59a22913ea5b1e532d8a707976997461ec143"))

			decoded.code mustEqual LpcMessageCode.GetBlocks.asByte
			decoded.blockHashes.size mustEqual 5
			decoded.blockHashes.head.toHexString mustEqual "497dcbd12fa99ced7b27cda6611f64eb13ab50e20260eec5ee6b7190e7206d54"
			decoded.blockHashes(4).toHexString mustEqual "b20f253d2b62609e932c13f3bca59a22913ea5b1e532d8a707976997461ec143"

			decoded.toEncodedBytes.toHexString mustEqual "f8a5a0497dcbd12fa99ced7b27cda6611f64eb13ab50e20260eec5ee6b7190e7206d54a00959bdfba5e54fcc9370e86b7996fbe32a277bab65c31a0102226f83c4d3e0f2a001a333c156485880776e929e84c26c9778c1e9b4dcb5cd3bff8ad0aeff385df0a0690e13595c9e8e4fa9a621dfed6ad828a6e8e591479af6897c979a83daf73084a0b20f253d2b62609e932c13f3bca59a22913ea5b1e532d8a707976997461ec143"
		}
	}

	"test NewBlockHashesMessage" should {
		"be right" in {
			val decoded: NewBlockHashesMessage = decodeMessage(LpcMessageCode.NewBlockHashes.asByte, ImmutableBytes.parseHexString("f91080a0a2ea288e8489386c8f3f432c495cd4afc660a76ae290faa79c3233a1b26e275da0eb993372faf0f89674100411e34132574aa7669f8e3d8cb1fb4b477de7239817a0d1392840fc28ce27b4e1cbb591059a96a2492796bd2e7916732fd2edb00580eba0cb63f815ef1090e0b3e39751f61eb0b552d46eab0107114886729fa626832488a0da8749cc4c9c9bc5ec56bd80fbc8b711b695789d6fa240e081b3f0bc89f4bb31a0deddf340863a1941ce6f2fd495c7715c2d64d7d7a2746df2581a21c3c52420dba0c3088b20debd70eb68d5e200570c76228bbb6bfdef58dba0aa3f8553c824c2b0a07fc7b9491917e18f71c7e469806c7fdfcca04c03aa2d8df4998a8d1674fbdc83a0087cee7e2ed5c9ea6a9535e81f3ac3062f99ab58ab7bc9069aff4e2060e2f599a09074112406d6db051a4ebdc8078b7d129dbb6a166673728bd58e404f17ea55f8a01169a089788773e1ec13d5d6b5c4557d7dde4962163ca57b949e9ac134a403a0a03f615177576ea228600a3e459fba7ccea7107e2688af564baddd1d9833ac1fb5a019ea09b84a3a88991dc2db502f4a4d9b5c968059dcf4de511e61564484e0b3e0a0cc59db5b35572a3b61fd86d28da5a4a418cf9ae003d412be0d25873608c1e1d5a064866985b925030855723bfb6b5d701babfde5129fbe346c87ec4ca5aa61e566a0406212c092afa109100c9fb2916abf15f331ba26c3c388604d11e443749a1b40a090b14e8d55761815622460a5a4d626456bcb7747e1904ec4adbad6c24782611ea0829e3488e8ba82d3f7b5381fbe9d8804548351caa334733fbe476aefb4130f4ca0dcbcca82612d2ef781f206a20fad89397722191164e025ccd337c502ba412373a02ae99ba9c72e94ed44ab05722767f3cbab8dbd1c01dd5a776a55b539c55b9e85a0583dcc22348644efde738c0d7b1a687f16af8d09be8df9275fef3fd392db31faa03d41c1e25712b64f6fb4233817c873b726ce90e302781178adf1a7911533d071a061c3b33345cfa7a5b6bac0f36fb32cdbc1b9d118e65057eb9865474d50e7f9e5a06ad335e45c1c066fcd38bb4a38480e32b8ee9e25e9c1de439e9cdbd330e364a2a01a0bd529b6de5eeea535bbdb6743a0a909cb231af0f07296cb7257642f4c68f8a0db6eb786bf3de22b73809b3f6d555e3997605f123cd257c6c7387243bd209ef7a0ee752c463b42fef910d1f39d55201e56e3c74031d3e49bd57d93cd162d28dcd9a04deb1ede0320acaf082a22899593e33ca4910351089d64cc83f9fbfa9f955951a0188989dbef1785ae91df968ab474ca23a0307dda95a3cc5539f3b11e326c83ada02518cf8ca3593b6443ac454e010d036cb4d89485f218daaeb7716f8aa89d07cca0518cee22a488628764552df13383f9e015cc667bf555363ba8f3ea0f8b85b7bfa0ac1203dc1aeaa96a0c75dcd39528ebbb259192bd6571f383beb9ec9b0861646da0a97223131e8b2e6ff3e52fa795ddee15b683dd8205421aa9f80ed91a1c90d170a0a2567683e988216fa5074ebe7aa17137131168348be89352e8bf2dc87a35aa60a06c6c9aba7cddd3cb92ad0350e611fff952ced700d61eb96b9686a6d686e30538a05957280b77eac9bfd6ef419d094a754a6f471ca05b9b8b3b83cf09cc6088ed18a07cab5dec5d043b0908090c5de057c6173de7dff1613a70697a0bfbe3b04da908a0571a35182af3635677bc901689b8b02e37360f6c2e06e840cf90d26c87475035a0f009d10e98790fcca5ccb98f94b41f1911c3ae19b60c1b7adf4de3921675dc05a088246732ecd3028b7fc699d4832250ba2b1ed42ea2fe1be54b54df1f4e4b862ba09565736b737f950931ffa5d1f640d223099f3d9e0ef22361102d62ffecc5209ba0412fdbdb7c859df743e27a327175091a33f134d6caa63faa2b36c61c9fe2fbb2a00f5eb7c40fb98cc9d5de98bb67232e86b82bc251def486abed9114b2c0343696a0e95b9206ed7cc556a3953e8fc3f735b6709b9781d570a226f1f189f5414317b8a028ee3cead3e297604d91dba76e20922f178efe9c09a199fcd51fe240569e77b6a0d4588ba5418fa506bcd31d5ff8ae2906b9e5f6c1113d7d57345fdd3e04da9a61a0c83a0a51f8a098bde13d01c1194cc3219ead64ab94ad802b223ef93ed27c649da051aae5a14ceb733fba7af3c9ee86da186a3c2103bb3f1b5bc9b5299c747e8071a02c2b00133aeb285ce5612b3214bf0565d49650c10fe614cbe3c887afa9900789a09dc4e7e4dc07110292ce843cbe22d6eed71bd25c7190d004cba056252f09079ea004c0f2c10f43f9148d77c47f00a3d829dc86ee04953c8673ca68d4cd42977a4ba06768cfaafd1f36478fefb588826d93608d577a1737ac5072e3a390d437804833a0d4d454cfa428bdf4152188b9dbba5919355d2f382c99c066267df34e7634da38a001cba9b78d1ee2bc0dea322f5f2f66f6a5dc4aca25d9ffa7be040b7092a4eeb7a0278b638b3a54c1853ba9435a45a94a9eb0a9717d7a2003736cd60bf3dd46810ea0d370591e3b53353975d023656c28af10961ac835f8a488c85b6e767d62db38b7a0ea99bdffd3ee26dcd564893ee37168fc452ea73e1d4d0d0326cedb1e6c9b2586a00b210d4c18a394f81da2b7a62d3e81a84ccd74bf78b107658d703201b70c4573a0ecbb14ad2ae320e1b7559a9a1847a56197e16784cd4be0475cf1ce2b22ae3b60a09990ce4bbc0f8e2beca7bf0d00caca340109827c0f0bad1d7c93295ab9be6278a02cd788c9aa9654f2abdf53a8c3069e687159a9f1639a6c74bc4dde044873a9bca0ab15e793e4e1745489400272218dc8fdd35278a83d13946ee918e5b9bef3d99ea0603cab6fabe95528dfe12d265b69caf5eee8563adf8b1865c8cc32c93fcfacc8a00cc85034eb44505e379a336e5636b245f85e1db285b7ffaaf9a6066c99a650e8a0427633406e21d0440c35b1998d2218a7f0891b0c2f8da0c2f7ba0c02e350d561a0fd9c2b4756197e2c9fdae9d4cbab541428246ec78a1c7a9289947080e1f1bbe8a0997ad58b0d6f62dd88e8b8adb5f224ee86a8e720b679f40d256decd91858643aa06aaa57c2b7145263637bfc22ae8ea0967f9663531bdad5fb1a5c102470773647a0d925ed216665a8d73e0c5a514d81c939f304244e7fc581b6e3e2a6d75290087aa0754c62cb9c10647aae99ee951bd05f884e8e77bf9922134625a1aa17ec289bcfa06b1525ab4987344561e83bd4a5e9fb0b2f25361f1368795daffffd8c2a32f1bca0df93b689d569879fec0c792c041b1c9f762b16950db7531e9756014714fbbd63a079c0b58d09884d1b6aecbe1850e1cbe5fc25510467143aba58a5b96161abe392a0a27b3f59f6248e2abb0f4d53898de806ba11aa0fc1bd293e76f888bc63a43035a010e9c21dd22d1706d8d863764293776f91fa875d862a32ee6284b8f09fe019d2a0d1dd30b8ba86dde88a360549a823bdf580979ac28646277c098fb9e6949b3dd1a04c351af31f3295e36532cd6dcd82481310e10c835048befd42be2eeccfd917cca070dbeb17933e584590bbda339a509b76a51b7a4a7aba5184c2b24c90f50f8894a07ba59a13f34b20e5092ba2720664bd70efc5351b475f4818d5edad2a1a121262a01e74cd965b11d7bbb1093deb0c6a881996cdae3095b1548011beb7689afcaaa2a028334b41abcdcee6d7e9c747e2034d90a99557405dccbe98e02acae8f8aaf42fa0d7a6f02d677888c30cbcec2dddb1a21be645320ba1189767000dc9413fed4112a0d3c10c6995a34ce16b6c38b82f8ceaa9abfac4989a9f8c3b50e99d8640eb31e1a0ed95eb4c460b3ae427408f7b9ba2f8a576000ca0ed24bb6920dcd1b74fb884d4a0018af52be5eddb01e86580dd0fbbf2414469f44dda3ff3da727a4c051257be0aa064860c2746d5b2a900a801e63ca7b82532383e4fd97fe0dbaacb6f7e0f6cf550a06169476aff1eb01c2d4e332b3513e4105141414227b0f6429247d89ea9fb750aa06f4b34fcc572b13e713317ab7a48722e3b295b5bfe0e7299a46c9e6ed66bc158a016d2bed98382a6987d60c6386ab60fc49425a427aac233da3d78d5c4c7fbe1e2a0e168b4f940d0199b4026e6f6081b684509ee995f59f5b05a758a96c75b060970a049ca29eb00f53d7c87091cf5de7750e32bef321d83fc7682db5f967c23b77c8fa0bb37817ebea8277da4ce4a79f3be3e55b13335db66be865e52a055f38c243a12a0850b7636637c43ee88cb8e06e4f8e245868a0b80e17ac9cb540548a9c2afaa67a00442a6f6f61e8d7b515978001ed2c85415918c5356e63da74bb1ef91c5cdd11aa07d6c2ccfe8a5b29cfe6aafc9a9444a9f3a7f49b7f8536756fb70bb127b4167e6a07a0ac54859232f30a9d41e5cf6dc169f4e6a665a5a418f8f4cd033ce0e366924a0150030708169754d146214c5c61b1817a2cd31c15d2d5921ff972d8d4b2a5e03a0be7703f359352389e86ee988f966658939ed91bbdbab8de758a1bb5f7264ae50a0b378f622a6ec3ac782dbc3753a873a577d2dd4e09d429c847ce1fac76efb13a0a081a7661248df3e7cf6f9b7b33e215ce531bf5d5a76fa1cddf03bde6737439794a0c17a31ff1011491df024f71a2c083594509d4ae8f9613ed987896779de905e27a0e5b9479581f4fd0d291b6bd8b626f14d09932e5196827e9862977ff5bc2a2b05a04f7db2d04363bb443afcba78bc5b7877ee2fe4ad32879884b4fa8148b19e5c6ba0ffd783176b13683fa2500a981e200fe9a68b00028384bf5c031c7e8a3aec2c44a01093f186e26d4da16d52df5b2dbff03921af688fc28c259065d6c4ce775c4e85a026e94c69fe404e1ceb571846462210396e1c09c5b5513b510edee01145f4b94fa02292b61bdb483d20363327ae7b73fa5e099e31dccb8cdb656e4d9ab5e9d03353a0b8c7d09ad0da7b9357511da2ee38f3ccf8411cc7e84d66b2bcf85dc352353ae6a0ec47f1b194cded4f16f264a5ab68edc1c6ecc0b6e4d61c5e0b200ce8ca44b2e5a022bb9ed039365ea12c936b7d9ca8a8d468a8e71f38bf2eff2ff36fdce81320dca0b97587d7820ff40b16b45528473f078acc342636344a4fad247e9ed31db6c669a0fd0844cdec6cc62a7b553a6f0162098af684525bb00719615fcf4c78ada664d9a061db1a8204298f10f1ddf095bcf05f6dfa86487d5d6dbb20009ebbb8224ae476a0bf8017cc3ecc317652e8c8b5ef751deff9ec43fa47bc72ff37d245c6aaccf337a07b4486bd942195ab46b3c697fda9b70e5269c0e6c72a219cda6e03cffa8b1e30a0f579efb941e26374191ec2f28dc27188521b10e19507e9311e6c686195c5c0fda0b213423d5eb5b813aab30a962985e3f706dc5f29af5e052191e1f38d26d80eb4a0a68f2cc38ff223615d9aa8c5dd1c53227aa822040313405de8905f3473d5b1cea0ee400f9dd50478ac55f07a6381fbb88f80c40844154b4c267d1bf16b5880149ba0b04a76de3b47d35c37221450de915eb9b0aed0a21523248cb826393e2ad20c95a033eb73cabac96c722c0aa0f1b8197d158c8226d9036f499d9b00cf289dd439d2a045179db4087149fb4e20a485b5cab8fe0c7147cb42c9eced73c540b0b8fc71c8a0d7dc5eacb31d4f8c3e8c947bf3b7fcbe63af3de317d1c8b956785436ac8aa0d2a0e9950e58d4b9a28aa526f3cbc4ed79f51bb860148c46cda9b538f3e6472c88e5a009b13139910a65611fd62cda2a8c4f3dea530ed2633deda8e563fb58d097dc40a060caece8f54edc9765f17be119351b8923ad73d1f1450a1e983fad79a09a6d35a06fb7ede9ecc2459ce4b2dfb335e63d1ffde8e98c0930ccff822443b370c61636a08de680978b8774a87add44d049bcf6780ec7f4eba79b4402799f6fc3a83755d7"))

			decoded.code mustEqual LpcMessageCode.NewBlockHashes.asByte
			decoded.blockHashes.size mustEqual 128

			decoded.toEncodedBytes.toHexString mustEqual "f91080a0a2ea288e8489386c8f3f432c495cd4afc660a76ae290faa79c3233a1b26e275da0eb993372faf0f89674100411e34132574aa7669f8e3d8cb1fb4b477de7239817a0d1392840fc28ce27b4e1cbb591059a96a2492796bd2e7916732fd2edb00580eba0cb63f815ef1090e0b3e39751f61eb0b552d46eab0107114886729fa626832488a0da8749cc4c9c9bc5ec56bd80fbc8b711b695789d6fa240e081b3f0bc89f4bb31a0deddf340863a1941ce6f2fd495c7715c2d64d7d7a2746df2581a21c3c52420dba0c3088b20debd70eb68d5e200570c76228bbb6bfdef58dba0aa3f8553c824c2b0a07fc7b9491917e18f71c7e469806c7fdfcca04c03aa2d8df4998a8d1674fbdc83a0087cee7e2ed5c9ea6a9535e81f3ac3062f99ab58ab7bc9069aff4e2060e2f599a09074112406d6db051a4ebdc8078b7d129dbb6a166673728bd58e404f17ea55f8a01169a089788773e1ec13d5d6b5c4557d7dde4962163ca57b949e9ac134a403a0a03f615177576ea228600a3e459fba7ccea7107e2688af564baddd1d9833ac1fb5a019ea09b84a3a88991dc2db502f4a4d9b5c968059dcf4de511e61564484e0b3e0a0cc59db5b35572a3b61fd86d28da5a4a418cf9ae003d412be0d25873608c1e1d5a064866985b925030855723bfb6b5d701babfde5129fbe346c87ec4ca5aa61e566a0406212c092afa109100c9fb2916abf15f331ba26c3c388604d11e443749a1b40a090b14e8d55761815622460a5a4d626456bcb7747e1904ec4adbad6c24782611ea0829e3488e8ba82d3f7b5381fbe9d8804548351caa334733fbe476aefb4130f4ca0dcbcca82612d2ef781f206a20fad89397722191164e025ccd337c502ba412373a02ae99ba9c72e94ed44ab05722767f3cbab8dbd1c01dd5a776a55b539c55b9e85a0583dcc22348644efde738c0d7b1a687f16af8d09be8df9275fef3fd392db31faa03d41c1e25712b64f6fb4233817c873b726ce90e302781178adf1a7911533d071a061c3b33345cfa7a5b6bac0f36fb32cdbc1b9d118e65057eb9865474d50e7f9e5a06ad335e45c1c066fcd38bb4a38480e32b8ee9e25e9c1de439e9cdbd330e364a2a01a0bd529b6de5eeea535bbdb6743a0a909cb231af0f07296cb7257642f4c68f8a0db6eb786bf3de22b73809b3f6d555e3997605f123cd257c6c7387243bd209ef7a0ee752c463b42fef910d1f39d55201e56e3c74031d3e49bd57d93cd162d28dcd9a04deb1ede0320acaf082a22899593e33ca4910351089d64cc83f9fbfa9f955951a0188989dbef1785ae91df968ab474ca23a0307dda95a3cc5539f3b11e326c83ada02518cf8ca3593b6443ac454e010d036cb4d89485f218daaeb7716f8aa89d07cca0518cee22a488628764552df13383f9e015cc667bf555363ba8f3ea0f8b85b7bfa0ac1203dc1aeaa96a0c75dcd39528ebbb259192bd6571f383beb9ec9b0861646da0a97223131e8b2e6ff3e52fa795ddee15b683dd8205421aa9f80ed91a1c90d170a0a2567683e988216fa5074ebe7aa17137131168348be89352e8bf2dc87a35aa60a06c6c9aba7cddd3cb92ad0350e611fff952ced700d61eb96b9686a6d686e30538a05957280b77eac9bfd6ef419d094a754a6f471ca05b9b8b3b83cf09cc6088ed18a07cab5dec5d043b0908090c5de057c6173de7dff1613a70697a0bfbe3b04da908a0571a35182af3635677bc901689b8b02e37360f6c2e06e840cf90d26c87475035a0f009d10e98790fcca5ccb98f94b41f1911c3ae19b60c1b7adf4de3921675dc05a088246732ecd3028b7fc699d4832250ba2b1ed42ea2fe1be54b54df1f4e4b862ba09565736b737f950931ffa5d1f640d223099f3d9e0ef22361102d62ffecc5209ba0412fdbdb7c859df743e27a327175091a33f134d6caa63faa2b36c61c9fe2fbb2a00f5eb7c40fb98cc9d5de98bb67232e86b82bc251def486abed9114b2c0343696a0e95b9206ed7cc556a3953e8fc3f735b6709b9781d570a226f1f189f5414317b8a028ee3cead3e297604d91dba76e20922f178efe9c09a199fcd51fe240569e77b6a0d4588ba5418fa506bcd31d5ff8ae2906b9e5f6c1113d7d57345fdd3e04da9a61a0c83a0a51f8a098bde13d01c1194cc3219ead64ab94ad802b223ef93ed27c649da051aae5a14ceb733fba7af3c9ee86da186a3c2103bb3f1b5bc9b5299c747e8071a02c2b00133aeb285ce5612b3214bf0565d49650c10fe614cbe3c887afa9900789a09dc4e7e4dc07110292ce843cbe22d6eed71bd25c7190d004cba056252f09079ea004c0f2c10f43f9148d77c47f00a3d829dc86ee04953c8673ca68d4cd42977a4ba06768cfaafd1f36478fefb588826d93608d577a1737ac5072e3a390d437804833a0d4d454cfa428bdf4152188b9dbba5919355d2f382c99c066267df34e7634da38a001cba9b78d1ee2bc0dea322f5f2f66f6a5dc4aca25d9ffa7be040b7092a4eeb7a0278b638b3a54c1853ba9435a45a94a9eb0a9717d7a2003736cd60bf3dd46810ea0d370591e3b53353975d023656c28af10961ac835f8a488c85b6e767d62db38b7a0ea99bdffd3ee26dcd564893ee37168fc452ea73e1d4d0d0326cedb1e6c9b2586a00b210d4c18a394f81da2b7a62d3e81a84ccd74bf78b107658d703201b70c4573a0ecbb14ad2ae320e1b7559a9a1847a56197e16784cd4be0475cf1ce2b22ae3b60a09990ce4bbc0f8e2beca7bf0d00caca340109827c0f0bad1d7c93295ab9be6278a02cd788c9aa9654f2abdf53a8c3069e687159a9f1639a6c74bc4dde044873a9bca0ab15e793e4e1745489400272218dc8fdd35278a83d13946ee918e5b9bef3d99ea0603cab6fabe95528dfe12d265b69caf5eee8563adf8b1865c8cc32c93fcfacc8a00cc85034eb44505e379a336e5636b245f85e1db285b7ffaaf9a6066c99a650e8a0427633406e21d0440c35b1998d2218a7f0891b0c2f8da0c2f7ba0c02e350d561a0fd9c2b4756197e2c9fdae9d4cbab541428246ec78a1c7a9289947080e1f1bbe8a0997ad58b0d6f62dd88e8b8adb5f224ee86a8e720b679f40d256decd91858643aa06aaa57c2b7145263637bfc22ae8ea0967f9663531bdad5fb1a5c102470773647a0d925ed216665a8d73e0c5a514d81c939f304244e7fc581b6e3e2a6d75290087aa0754c62cb9c10647aae99ee951bd05f884e8e77bf9922134625a1aa17ec289bcfa06b1525ab4987344561e83bd4a5e9fb0b2f25361f1368795daffffd8c2a32f1bca0df93b689d569879fec0c792c041b1c9f762b16950db7531e9756014714fbbd63a079c0b58d09884d1b6aecbe1850e1cbe5fc25510467143aba58a5b96161abe392a0a27b3f59f6248e2abb0f4d53898de806ba11aa0fc1bd293e76f888bc63a43035a010e9c21dd22d1706d8d863764293776f91fa875d862a32ee6284b8f09fe019d2a0d1dd30b8ba86dde88a360549a823bdf580979ac28646277c098fb9e6949b3dd1a04c351af31f3295e36532cd6dcd82481310e10c835048befd42be2eeccfd917cca070dbeb17933e584590bbda339a509b76a51b7a4a7aba5184c2b24c90f50f8894a07ba59a13f34b20e5092ba2720664bd70efc5351b475f4818d5edad2a1a121262a01e74cd965b11d7bbb1093deb0c6a881996cdae3095b1548011beb7689afcaaa2a028334b41abcdcee6d7e9c747e2034d90a99557405dccbe98e02acae8f8aaf42fa0d7a6f02d677888c30cbcec2dddb1a21be645320ba1189767000dc9413fed4112a0d3c10c6995a34ce16b6c38b82f8ceaa9abfac4989a9f8c3b50e99d8640eb31e1a0ed95eb4c460b3ae427408f7b9ba2f8a576000ca0ed24bb6920dcd1b74fb884d4a0018af52be5eddb01e86580dd0fbbf2414469f44dda3ff3da727a4c051257be0aa064860c2746d5b2a900a801e63ca7b82532383e4fd97fe0dbaacb6f7e0f6cf550a06169476aff1eb01c2d4e332b3513e4105141414227b0f6429247d89ea9fb750aa06f4b34fcc572b13e713317ab7a48722e3b295b5bfe0e7299a46c9e6ed66bc158a016d2bed98382a6987d60c6386ab60fc49425a427aac233da3d78d5c4c7fbe1e2a0e168b4f940d0199b4026e6f6081b684509ee995f59f5b05a758a96c75b060970a049ca29eb00f53d7c87091cf5de7750e32bef321d83fc7682db5f967c23b77c8fa0bb37817ebea8277da4ce4a79f3be3e55b13335db66be865e52a055f38c243a12a0850b7636637c43ee88cb8e06e4f8e245868a0b80e17ac9cb540548a9c2afaa67a00442a6f6f61e8d7b515978001ed2c85415918c5356e63da74bb1ef91c5cdd11aa07d6c2ccfe8a5b29cfe6aafc9a9444a9f3a7f49b7f8536756fb70bb127b4167e6a07a0ac54859232f30a9d41e5cf6dc169f4e6a665a5a418f8f4cd033ce0e366924a0150030708169754d146214c5c61b1817a2cd31c15d2d5921ff972d8d4b2a5e03a0be7703f359352389e86ee988f966658939ed91bbdbab8de758a1bb5f7264ae50a0b378f622a6ec3ac782dbc3753a873a577d2dd4e09d429c847ce1fac76efb13a0a081a7661248df3e7cf6f9b7b33e215ce531bf5d5a76fa1cddf03bde6737439794a0c17a31ff1011491df024f71a2c083594509d4ae8f9613ed987896779de905e27a0e5b9479581f4fd0d291b6bd8b626f14d09932e5196827e9862977ff5bc2a2b05a04f7db2d04363bb443afcba78bc5b7877ee2fe4ad32879884b4fa8148b19e5c6ba0ffd783176b13683fa2500a981e200fe9a68b00028384bf5c031c7e8a3aec2c44a01093f186e26d4da16d52df5b2dbff03921af688fc28c259065d6c4ce775c4e85a026e94c69fe404e1ceb571846462210396e1c09c5b5513b510edee01145f4b94fa02292b61bdb483d20363327ae7b73fa5e099e31dccb8cdb656e4d9ab5e9d03353a0b8c7d09ad0da7b9357511da2ee38f3ccf8411cc7e84d66b2bcf85dc352353ae6a0ec47f1b194cded4f16f264a5ab68edc1c6ecc0b6e4d61c5e0b200ce8ca44b2e5a022bb9ed039365ea12c936b7d9ca8a8d468a8e71f38bf2eff2ff36fdce81320dca0b97587d7820ff40b16b45528473f078acc342636344a4fad247e9ed31db6c669a0fd0844cdec6cc62a7b553a6f0162098af684525bb00719615fcf4c78ada664d9a061db1a8204298f10f1ddf095bcf05f6dfa86487d5d6dbb20009ebbb8224ae476a0bf8017cc3ecc317652e8c8b5ef751deff9ec43fa47bc72ff37d245c6aaccf337a07b4486bd942195ab46b3c697fda9b70e5269c0e6c72a219cda6e03cffa8b1e30a0f579efb941e26374191ec2f28dc27188521b10e19507e9311e6c686195c5c0fda0b213423d5eb5b813aab30a962985e3f706dc5f29af5e052191e1f38d26d80eb4a0a68f2cc38ff223615d9aa8c5dd1c53227aa822040313405de8905f3473d5b1cea0ee400f9dd50478ac55f07a6381fbb88f80c40844154b4c267d1bf16b5880149ba0b04a76de3b47d35c37221450de915eb9b0aed0a21523248cb826393e2ad20c95a033eb73cabac96c722c0aa0f1b8197d158c8226d9036f499d9b00cf289dd439d2a045179db4087149fb4e20a485b5cab8fe0c7147cb42c9eced73c540b0b8fc71c8a0d7dc5eacb31d4f8c3e8c947bf3b7fcbe63af3de317d1c8b956785436ac8aa0d2a0e9950e58d4b9a28aa526f3cbc4ed79f51bb860148c46cda9b538f3e6472c88e5a009b13139910a65611fd62cda2a8c4f3dea530ed2633deda8e563fb58d097dc40a060caece8f54edc9765f17be119351b8923ad73d1f1450a1e983fad79a09a6d35a06fb7ede9ecc2459ce4b2dfb335e63d1ffde8e98c0930ccff822443b370c61636a08de680978b8774a87add44d049bcf6780ec7f4eba79b4402799f6fc3a83755d7"
		}
	}

	"test NewBlockMessage" should {
		"be right" in {
			val decoded: NewBlockMessage = decodeMessage(LpcMessageCode.NewBlock.asByte, ImmutableBytes.parseHexString("f90144f9013bf90136a0d8faffbc4c4213d35db9007de41cece45d95db7fd6c0f129e158baa888c48eefa01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d4934794baedba0480e1b882b606cd302d8c4f5701cabac7a0c7d4565fb7b3d98e54a0dec8b76f8c001a784a5689954ce0aedcc1bbe8d13095a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b8400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000083063477825fc88609184e72a0008301e8488084543ffee680a00de0b9d4a0f0c23546d31f1f70db00d25cf6a7af79365b4e058e4a6a3b69527bc0c0850177ddbebe"))

			decoded.code mustEqual LpcMessageCode.NewBlock.asByte

			decoded.toEncodedBytes.toHexString mustEqual "f90144f9013bf90136a0d8faffbc4c4213d35db9007de41cece45d95db7fd6c0f129e158baa888c48eefa01dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d4934794baedba0480e1b882b606cd302d8c4f5701cabac7a0c7d4565fb7b3d98e54a0dec8b76f8c001a784a5689954ce0aedcc1bbe8d13095a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421a056e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421b8400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000083063477825fc88609184e72a0008301e8488084543ffee680a00de0b9d4a0f0c23546d31f1f70db00d25cf6a7af79365b4e058e4a6a3b69527bc0c0850177ddbebe"
		}
	}

	"test StatusMessage" should {
		"be right" in {
			val decoded: StatusMessage = decodeMessage(LpcMessageCode.Status.asByte, ImmutableBytes.parseHexString("f84927808425c60144a0832056d3c93ff2739ace7199952e5365aa29f18805be05634c4db125c5340216a0955f36d073ccb026b78ab3424c15cf966a7563aa270413859f78702b9e8e22cb"))

			decoded.code mustEqual LpcMessageCode.Status.asByte
			decoded.protocolVersion mustEqual 39
			decoded.totalDifficulty.toHexString mustEqual "25c60144"
			decoded.bestHash.toHexString mustEqual "832056d3c93ff2739ace7199952e5365aa29f18805be05634c4db125c5340216"

			decoded.toEncodedBytes.toHexString mustEqual "f84927808425c60144a0832056d3c93ff2739ace7199952e5365aa29f18805be05634c4db125c5340216a0955f36d073ccb026b78ab3424c15cf966a7563aa270413859f78702b9e8e22cb"
		}
	}

	"test TransactionsMessage" should {
		"be right" in {
			val hex = "f86df86b04648609184e72a00094cd2a3d9f938e13cd947ec05abc7fe734df8dd826881bc16d674ec80000801ba05c89ebf2b77eeab88251e553f6f9d53badc1d800bbac02d830801c2aa94a4c9fa00b7907532b1f29c79942b75fff98822293bf5fdaa3653a8d9f424c6a3265f06c"
			val decoded: TransactionsMessage = decodeMessage(LpcMessageCode.Transactions.asByte, ImmutableBytes.parseHexString(hex))

			decoded.code mustEqual LpcMessageCode.Transactions.asByte
			decoded.transactions.size mustEqual 1
			decoded.transactions.head.hash.toHexString mustEqual "12fede795a80286ce4a100ce658143dc0b3e13db7696f90442b46f21981e88dc"

			decoded.toEncodedBytes.toHexString mustEqual hex
		}
	}


	 private def decodeMessage[T](byte: Byte, bytes: ImmutableBytes): T = {
		 (new LpcMessageFactory).create(byte, bytes).get.asInstanceOf[T]
	 }
 }
